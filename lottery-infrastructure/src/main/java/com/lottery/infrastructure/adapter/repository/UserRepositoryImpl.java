package com.lottery.infrastructure.adapter.repository;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.ExchangeTokenSetWithQRcodeTicketDto;
import cn.authing.sdk.java.dto.GenerateQrcodeDto;
import cn.authing.sdk.java.dto.GeneQRCodeRespDto;
import cn.authing.sdk.java.dto.LoginTokenRespDto;
import cn.authing.sdk.java.dto.SendEmailDto;
import cn.authing.sdk.java.dto.SendEmailRespDto;
import cn.authing.sdk.java.dto.SignUpOptionsDto;
import cn.authing.sdk.java.dto.SignUpProfileDto;
import cn.authing.sdk.java.dto.UserSingleRespDto;
import cn.authing.sdk.java.dto.authentication.UserInfo;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.model.vo.WechatMiniProgramQrCodeVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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
        try {
            LoginRpcRequest loginRequest = LoginRpcRequest.newBuilder()
                    .setUsername(email)
                    .setPassword(password)
                    .setTenantId(tenantId)
                    .build();

            AuthResult authResult = authRpcService.authenticate(loginRequest);
            if (!authResult.getSuccess()) {
                throw new AppException(ResponseCode.EMAIL_PASSWORD_INVALID.getCode(), ResponseCode.EMAIL_PASSWORD_INVALID.getMessage());
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
            log.error("email login rpc invocation failed, email={}", email, e);
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
    public WechatMiniProgramQrCodeVO generateWechatMiniProgramLoginQrCode() {
        try {
            AuthenticationClient client = newAuthenticationClient();
            GenerateQrcodeDto request = new GenerateQrcodeDto();
            request.setType(GenerateQrcodeDto.Type.WECHAT_MINIPROGRAM);

            GeneQRCodeRespDto response = client.geneQrCode(request);
            ensureAuthingSuccess(response.getStatusCode(), response.getMessage(), ResponseCode.AUTHING_SERVICE_ERROR);

            WechatMiniProgramQrCodeVO qrCodeVO = new WechatMiniProgramQrCodeVO();
            if (response.getData() != null) {
                qrCodeVO.setQrcodeId(response.getData().getQrcodeId());
                qrCodeVO.setQrCodeUrl(response.getData().getUrl());
//                qrCodeVO.setCustomLogoUrl(response.getData().getCustomLogoUrl());
            }
            qrCodeVO.setStatus("PENDING");
            return qrCodeVO;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("generate wechat mini program login qrcode failed", e);
            throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(), ResponseCode.AUTHING_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public WechatMiniProgramQrCodeVO queryWechatMiniProgramLoginQrCodeStatus(String qrcodeId) {
        try {
            JSONObject response = doAuthingGet("/api/v3/check-qrcode-status?qrcodeId="
                    + URLEncoder.encode(qrcodeId, StandardCharsets.UTF_8));
            JSONObject data = resolveQrCodeStatusData(response);
            Integer statusCode = response.getInteger("statusCode");
            String message = response.getString("message");
            if (data == null) {
                log.error("query wechat mini program login qrcode status failed, qrcodeId={}, authingStatusCode={}, authingMessage={}, rawResponse={}",
                        qrcodeId, statusCode, message, response.toJSONString());
                throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(),
                        buildAuthingErrorMessage(ResponseCode.AUTHING_SERVICE_ERROR, message));
            }

            WechatMiniProgramQrCodeVO qrCodeVO = new WechatMiniProgramQrCodeVO();
            qrCodeVO.setQrcodeId(qrcodeId);
            qrCodeVO.setStatus(data.getString("status"));
            qrCodeVO.setTicket(data.getString("ticket"));
            JSONObject briefUserInfo = data.getJSONObject("briefUserInfo");
            if (briefUserInfo != null) {
                qrCodeVO.setDisplayName(briefUserInfo.getString("displayName"));
                qrCodeVO.setPhoto(briefUserInfo.getString("photo"));
            }
            log.info("query wechat mini program login qrcode status succeeded, qrcodeId={}, authingStatus={}, ticketPresent={}",
                    qrcodeId, qrCodeVO.getStatus(), StringUtils.isNotBlank(qrCodeVO.getTicket()));
            return qrCodeVO;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("query wechat mini program login qrcode status failed, qrcodeId={}", qrcodeId, e);
            throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(), ResponseCode.AUTHING_SERVICE_ERROR.getMessage(), e);
        }
    }

    @Override
    public UserVO loginByWechatMiniProgramQrCode(String ticket) {
        try {
            AuthenticationClient client = newAuthenticationClient();
            ExchangeTokenSetWithQRcodeTicketDto request = new ExchangeTokenSetWithQRcodeTicketDto();
            request.setTicket(ticket);
            request.setClientId(authingAppId);
            request.setClientSecret(authingAppSecret);

            LoginTokenRespDto tokenResponse = client.exchangeTokenSetWithQrCodeTicket(request);
            ensureAuthingSuccess(tokenResponse.getStatusCode(), tokenResponse.getMessage(), ResponseCode.AUTHING_SERVICE_ERROR);
            if (tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getAccessToken())) {
                throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(), ResponseCode.AUTHING_SERVICE_ERROR.getMessage());
            }

            UserInfo authingUserInfo = client.getUserInfoByAccessToken(tokenResponse.getData().getAccessToken());
            if (authingUserInfo == null || StringUtils.isBlank(authingUserInfo.getSub())) {
                throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(), "authing user info is empty");
            }

            String localUsername = buildWechatMiniProgramUsername(authingUserInfo.getSub());
            UserRpcResponse user = loadUserByUsername(localUsername);
            if (user == null || user.getId() <= 0) {
                user = createWechatMiniProgramUser(localUsername, authingUserInfo);
            }

            if (user == null || user.getId() <= 0) {
                throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage());
            }

            return buildLoginUser(user.getId(), resolveUsername(user, localUsername));
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("wechat mini program qrcode login failed", e);
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

    private UserVO buildLoginUser(Long userId, String username) {
        TokenRpcResponse tokenResponse = generateToken(userId, tenantId);
        storeRefreshToken(tenantId, userId, tokenResponse.getRefreshToken());

        UserVO userVO = new UserVO();
        userVO.setId(userId);
        userVO.setUsername(username);
        userVO.setToken(tokenResponse.getAccessToken());
        userVO.setRefreshToken(tokenResponse.getRefreshToken());
        userVO.setExpiresIn(tokenResponse.getExpiresIn());
        return userVO;
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

    private JSONObject doAuthingGet(String pathWithQuery) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authingAppHost + pathWithQuery))
                .header("x-authing-request-from", "java-sdk")
                .header("x-authing-sdk-version", "1.0.0")
                .header("x-authing-app-id", authingAppId)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            log.error("authing http get failed, path={}, httpStatus={}, body={}", pathWithQuery, response.statusCode(), response.body());
            throw new AppException(ResponseCode.AUTHING_SERVICE_ERROR.getCode(),
                    buildAuthingErrorMessage(ResponseCode.AUTHING_SERVICE_ERROR, response.body()));
        }

        return JSON.parseObject(response.body());
    }

    private JSONObject resolveQrCodeStatusData(JSONObject response) {
        if (response == null) {
            return null;
        }
        JSONObject data = response.getJSONObject("data");
        if (data != null) {
            return data;
        }
        if (response.containsKey("status")) {
            return response;
        }
        return null;
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

    private String buildWechatMiniProgramUsername(String authingSub) {
        return "wxmp_" + DigestUtils.md5Hex(authingSub);
    }

    private String resolveWechatMiniProgramNickname(UserInfo userInfo, String fallbackUsername) {
        if (userInfo == null) {
            return fallbackUsername;
        }
        if (StringUtils.isNotBlank(userInfo.getNickname())) {
            return userInfo.getNickname();
        }
        if (StringUtils.isNotBlank(userInfo.getName())) {
            return userInfo.getName();
        }
        if (StringUtils.isNotBlank(userInfo.getPreferredUsername())) {
            return userInfo.getPreferredUsername();
        }
        return fallbackUsername;
    }

    private UserRpcResponse createWechatMiniProgramUser(String username, UserInfo userInfo) {
        String password = RandomStringUtils.randomAlphanumeric(32);
        String nickname = resolveWechatMiniProgramNickname(userInfo, username);

        try {
            RegisterRpcResult registerResult = authRpcService.register(RegisterRpcRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setTenantId(tenantId)
                    .setNickname(nickname)
                    .build());
            if (!registerResult.getSuccess()) {
                AppException registerException = toRegisterException(registerResult.getMessage());
                if (registerException.getCode() != ResponseCode.EMAIL_ALREADY_REGISTERED.getCode()) {
                    throw registerException;
                }
            } else if (registerResult.hasUser()) {
                return registerResult.getUser();
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("create wechat mini program user failed, username={}", username, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), ResponseCode.AUTH_SERVICE_ERROR.getMessage(), e);
        }

        return loadUserByUsername(username);
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
