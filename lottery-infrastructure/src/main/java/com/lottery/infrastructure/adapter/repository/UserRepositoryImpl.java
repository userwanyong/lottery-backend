package com.lottery.infrastructure.adapter.repository;

import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.AuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.Int64Value;
import cn.wanyj.auth.api.protobuf.LoginRpcRequest;
import cn.wanyj.auth.api.protobuf.StringValue;
import cn.wanyj.auth.api.protobuf.TokenGenerationRequest;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
        try {
            LoginRpcRequest loginRequest = LoginRpcRequest.newBuilder()
                    .setUsername(userDTO.getUsername())
                    .setPassword(userDTO.getPassword())
                    .setTenantId(tenantId)
                    .build();

            AuthResult authResult = authRpcService.authenticate(loginRequest);
            if (!authResult.getSuccess()) {
                throw new AppException(ResponseCode.LOGIN_FAILED.getCode(), ResponseCode.LOGIN_FAILED.getMessage());
            }

            TokenRpcResponse tokenResponse = generateToken(authResult.getUserId(), tenantId);
            storeRefreshToken(tenantId, authResult.getUserId(), tokenResponse.getRefreshToken());

            UserVO userVO = new UserVO();
            userVO.setId(authResult.getUserId());
            userVO.setUsername(authResult.getUsername());
            userVO.setToken(tokenResponse.getAccessToken());
            userVO.setRefreshToken(tokenResponse.getRefreshToken());
            userVO.setExpiresIn(tokenResponse.getExpiresIn());
            return userVO;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("login rpc invocation failed, username={}", userDTO.getUsername(), e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        try {
            TokenValidationResult validationResult = tokenRpcService.parseToken(
                    StringValue.newBuilder().setValue(refreshToken).build()
            );
            if (!validationResult.getValid()) {
                throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
            }

            long userId = validationResult.getUserId();
            long currentTenantId = validationResult.getTenantId() > 0 ? validationResult.getTenantId() : tenantId;
            String storedRefreshToken = redisService.getValue(buildRefreshTokenKey(currentTenantId, userId));
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
            }

            TokenRpcResponse tokenResponse = generateToken(userId, currentTenantId);
            storeRefreshToken(currentTenantId, userId, tokenResponse.getRefreshToken());

            UserVO userVO = new UserVO();
            userVO.setId(userId);
            userVO.setUsername(validationResult.getUsername());
            userVO.setToken(tokenResponse.getAccessToken());
            userVO.setRefreshToken(tokenResponse.getRefreshToken());
            userVO.setExpiresIn(tokenResponse.getExpiresIn());
            return userVO;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("refresh token rpc invocation failed", e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public void logout(Long userId) {
        try {
            tokenRpcService.revokeAllTokens(Int64Value.newBuilder().setValue(userId).build());
        } catch (Exception e) {
            log.error("revoke all tokens failed, userId={}", userId, e);
        }

        redisService.remove(buildRefreshTokenKey(tenantId, userId));
    }

    private TokenRpcResponse generateToken(Long userId, Long currentTenantId) {
        TokenGenerationRequest tokenRequest = TokenGenerationRequest.newBuilder()
                .setUserId(userId)
                .setTenantId(currentTenantId)
                .build();
        return tokenRpcService.generateToken(tokenRequest);
    }

    private void storeRefreshToken(Long currentTenantId, Long userId, String refreshToken) {
        long ttlMillis = TimeUnit.DAYS.toMillis(refreshTokenTtlDays);
        redisService.setValue(buildRefreshTokenKey(currentTenantId, userId), refreshToken, ttlMillis);
    }

    private String buildRefreshTokenKey(Long currentTenantId, Long userId) {
        return refreshTokenPrefix + currentTenantId + ":" + userId;
    }
}
