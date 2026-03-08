package com.lottery.infrastructure.adapter.repository;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.SendEmailDto;
import cn.authing.sdk.java.dto.SendEmailRespDto;
import cn.authing.sdk.java.dto.SignUpOptionsDto;
import cn.authing.sdk.java.dto.SignUpProfileDto;
import cn.authing.sdk.java.dto.UserSingleRespDto;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.AuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.Int64Value;
import cn.wanyj.auth.api.protobuf.LoginRpcRequest;
import cn.wanyj.auth.api.protobuf.RegisterRpcRequest;
import cn.wanyj.auth.api.protobuf.RegisterRpcResult;
import cn.wanyj.auth.api.protobuf.StringValue;
import cn.wanyj.auth.api.protobuf.TokenGenerationRequest;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import cn.wanyj.auth.api.protobuf.UserByUsernameRequest;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
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

    @Value("${authing.app-id}")
    private String authingAppId;

    @Value("${authing.app-secret}")
    private String authingAppSecret;

    @Value("${authing.app-host}")
    private String authingAppHost;

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
    public void sendEmailRegisterCode(String email) {
        if (isRegisteredUsername(email)) {
            throw new AppException(ResponseCode.EMAIL_ALREADY_REGISTERED.getCode(), ResponseCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }

        try {
            AuthenticationClient client = newAuthenticationClient();
            SendEmailDto request = new SendEmailDto();
            request.setEmail(email);
            request.setChannel(SendEmailDto.Channel.CHANNEL_REGISTER);

            SendEmailRespDto response = client.sendEmail(request);
            ensureAuthingSuccess(response.getStatusCode(), response.getMessage(), ResponseCode.AUTHING_SEND_EMAIL_FAILED);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("send authing register email code failed, email={}", email, e);
            throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(), ResponseCode.AUTHING_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public UserVO registerByEmail(String email, String passCode, String password) {
        if (isRegisteredUsername(email)) {
            throw new AppException(ResponseCode.EMAIL_ALREADY_REGISTERED.getCode(), ResponseCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }

        try {
            AuthenticationClient client = newAuthenticationClient();
            UserSingleRespDto signUpResponse = client.signUpByEmailPassCode(
                    email,
                    passCode,
                    buildSignUpProfile(email),
                    new SignUpOptionsDto()
            );
            ensureAuthingSuccess(signUpResponse.getStatusCode(), signUpResponse.getMessage(), ResponseCode.AUTHING_EMAIL_SIGN_UP_FAILED);

            RegisterRpcResult registerResult = authRpcService.register(RegisterRpcRequest.newBuilder()
                    .setUsername(email)
                    .setPassword(password)
                    .setTenantId(tenantId)
                    .setEmail(email)
                    .setNickname(resolveEmailNickname(signUpResponse.getData(), email))
                    .build());

            if (!registerResult.getSuccess()) {
                throw toRegisterException(registerResult.getMessage());
            }

            UserRpcResponse user = registerResult.hasUser() ? registerResult.getUser() : loadUserByUsername(email);
            if (user == null || user.getId() <= 0) {
                throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage());
            }

            TokenRpcResponse tokenResponse = registerResult.hasToken()
                    ? registerResult.getToken()
                    : generateToken(user.getId(), tenantId);
            storeRefreshToken(tenantId, user.getId(), tokenResponse.getRefreshToken());

            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(resolveUsername(user, email));
            userVO.setToken(tokenResponse.getAccessToken());
            userVO.setRefreshToken(tokenResponse.getRefreshToken());
            userVO.setExpiresIn(tokenResponse.getExpiresIn());
            return userVO;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("authing email register failed, email={}", email, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public UserVO loginByEmailPassword(String email, String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(email);
        userDTO.setPassword(password);
        return login(userDTO);
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

    private AuthenticationClient newAuthenticationClient() throws IOException, ParseException {
        AuthenticationClientOptions options = new AuthenticationClientOptions();
        options.setAppId(authingAppId);
        options.setAppSecret(authingAppSecret);
        options.setAppHost(authingAppHost);
        return new AuthenticationClient(options);
    }

    private SignUpProfileDto buildSignUpProfile(String email) {
        SignUpProfileDto profile = new SignUpProfileDto();
        profile.setPreferredUsername(email);
        profile.setNickname(email);
        return profile;
    }

    private boolean isRegisteredUsername(String username) {
        UserRpcResponse user = loadUserByUsername(username);
        return user != null && user.getId() > 0;
    }

    private UserRpcResponse loadUserByUsername(String username) {
        return authRpcService.getUserByUsername(UserByUsernameRequest.newBuilder()
                .setUsername(username)
                .setTenantId(tenantId)
                .build());
    }

    private String resolveEmailNickname(cn.authing.sdk.java.dto.UserDto user, String fallbackEmail) {
        if (user == null) {
            return fallbackEmail;
        }
        if (StringUtils.isNotBlank(user.getNickname())) {
            return user.getNickname();
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            return user.getUsername();
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            return user.getEmail();
        }
        return fallbackEmail;
    }

    private String resolveUsername(UserRpcResponse user, String fallbackUsername) {
        if (user == null) {
            return fallbackUsername;
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            return user.getUsername();
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            return user.getEmail();
        }
        if (StringUtils.isNotBlank(user.getNickname())) {
            return user.getNickname();
        }
        return fallbackUsername;
    }

    private AppException toRegisterException(String message) {
        String normalizedMessage = StringUtils.lowerCase(StringUtils.defaultString(message));
        if (normalizedMessage.contains("exists")
                || normalizedMessage.contains("exist")
                || normalizedMessage.contains("duplicate")
                || normalizedMessage.contains("already")) {
            return new AppException(ResponseCode.EMAIL_ALREADY_REGISTERED.getCode(), ResponseCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }
        return new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), buildAuthingErrorMessage(ResponseCode.AUTH_SERVICE_ERROR, message));
    }

    private boolean isAuthingSuccess(Integer statusCode) {
        return statusCode != null && statusCode == 200;
    }

    private void ensureAuthingSuccess(Integer statusCode, String message, ResponseCode responseCode) {
        if (!isAuthingSuccess(statusCode)) {
            throw new AppException(responseCode.getCode(), buildAuthingErrorMessage(responseCode, message));
        }
    }

    private String buildAuthingErrorMessage(ResponseCode responseCode, String message) {
        return StringUtils.isBlank(message) ? responseCode.getMessage() : responseCode.getMessage() + ": " + message;
    }
}
