package com.lottery.infrastructure.adapter.repository;


import cn.wanyj.auth.api.protobuf.*;
import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author 永
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @DubboReference(version = "1.0.0", check = false)
    private AuthRpcServiceProtobuf authRpcService;

    @DubboReference(version = "1.0.0", check = false)
    private TokenRpcServiceProtobuf tokenRpcService;

    @Resource
    private RedisService redisService;

    @Value("${auth.tenant-id:1}")
    private Long tenantId;

    @Value("${auth.refresh-token-prefix:lottery:refresh_token:}")
    private String refreshTokenPrefix;

    @Value("${auth.refresh-token-ttl-days:7}")
    private Integer refreshTokenTtlDays;

    @Override
    public UserVO login(UserDTO userDTO) {
        // 1. 通过RPC调用auth-service进行认证
        LoginRpcRequest loginRequest = LoginRpcRequest.newBuilder()
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword())
                .setTenantId(tenantId)
                .build();

        AuthResult authResult = authRpcService.authenticate(loginRequest);
        if (!authResult.getSuccess()) {
            throw new RuntimeException(authResult.getMessage());
        }

        // 2. 认证成功，生成token
        TokenGenerationRequest tokenRequest = TokenGenerationRequest.newBuilder()
                .setUserId(authResult.getUserId())
                .setTenantId(tenantId)
                .build();

        TokenRpcResponse tokenResponse = tokenRpcService.generateToken(tokenRequest);

        // 3. 将refresh token存入Redis
        String redisKey = refreshTokenPrefix + tenantId + ":" + authResult.getUserId();
        long ttlMillis = TimeUnit.DAYS.toMillis(refreshTokenTtlDays);
        redisService.setValue(redisKey, tokenResponse.getRefreshToken(), ttlMillis);

        // 4. 构建返回VO
        UserVO userVO = new UserVO();
        userVO.setId(authResult.getUserId());
        userVO.setUsername(authResult.getUsername());
        userVO.setToken(tokenResponse.getAccessToken());
        userVO.setRefreshToken(tokenResponse.getRefreshToken());
        userVO.setExpiresIn(tokenResponse.getExpiresIn());
        return userVO;
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        // 1. 解析refresh token中的userId和tenantId（从JWT payload base64解码）
        Long userId;
        Long tokenTenantId;
        try {
            // JWT结构: header.payload.signature
            String[] parts = refreshToken.split("\\.");
            if (parts.length < 2) {
                throw new RuntimeException("无效的refresh token格式");
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            // 简单解析JSON获取userId和tenantId
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(payload);
            userId = jsonObject.getLong("userId");
            tokenTenantId = jsonObject.getLong("tenantId");
            if (userId == null) {
                throw new RuntimeException("refresh token中未包含userId");
            }
            if (tokenTenantId == null) {
                tokenTenantId = tenantId;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("refresh token解析失败", e);
        }

        // 2. 验证Redis中存储的refresh token
        String redisKey = refreshTokenPrefix + tokenTenantId + ":" + userId;
        String storedRefreshToken = redisService.getValue(redisKey);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("refresh token无效或已过期");
        }

        // 3. 生成新的token对
        TokenGenerationRequest tokenRequest = TokenGenerationRequest.newBuilder()
                .setUserId(userId)
                .setTenantId(tokenTenantId)
                .build();

        TokenRpcResponse tokenResponse = tokenRpcService.generateToken(tokenRequest);

        // 4. 更新Redis中的refresh token
        long ttlMillis = TimeUnit.DAYS.toMillis(refreshTokenTtlDays);
        redisService.setValue(redisKey, tokenResponse.getRefreshToken(), ttlMillis);

        // 5. 构建返回VO
        UserVO userVO = new UserVO();
        userVO.setId(userId);
        userVO.setToken(tokenResponse.getAccessToken());
        userVO.setRefreshToken(tokenResponse.getRefreshToken());
        userVO.setExpiresIn(tokenResponse.getExpiresIn());
        return userVO;
    }

    @Override
    public void logout(Long userId) {
        // 1. 通过RPC撤销所有token
        try {
            tokenRpcService.revokeAllTokens(
                    Int64Value.newBuilder().setValue(userId).build()
            );
        } catch (Exception e) {
            log.error("调用auth-service撤销token失败", e);
        }

        // 2. 删除Redis中的refresh token
        String redisKey = refreshTokenPrefix + tenantId + ":" + userId;
        redisService.remove(redisKey);
    }
}
