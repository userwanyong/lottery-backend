package com.lottery.infrastructure.adapter.repository;

import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthUrlRpcResponse;
import cn.wanyj.auth.api.protobuf.OperationResult;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.adapter.rpc.AuthServiceGateway;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 用户认证仓储实现：全部委托 auth-service（Dubbo Triple RPC），
 * refresh token 的存储与轮换由 auth-service 统一管理，本服务不再落 Redis。
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Resource
    private AuthServiceGateway authServiceGateway;

    @Override
    public List<String> listEnabledLoginMethods() {
        List<String> methods = authServiceGateway.listEnabledLoginMethods();
        return methods == null ? Collections.emptyList() : methods;
    }

    @Override
    public UserVO loginByPassword(String username, String password) {
        AuthResult authResult = authServiceGateway.authenticate(username, password);
        if (!authResult.getSuccess()) {
            log.warn("password login rejected by auth-service, username={}, message={}", username, authResult.getMessage());
            throw new AppException(ResponseCode.LOGIN_FAILED.getCode(),
                    resolveMessage(authResult.getMessage(), ResponseCode.LOGIN_FAILED));
        }

        UserRpcResponse user = authServiceGateway.getUserById(authResult.getUserId());
        if (user.getId() <= 0) {
            throw new AppException(ResponseCode.LOGIN_FAILED.getCode(), ResponseCode.LOGIN_FAILED.getMessage());
        }

        TokenRpcResponse tokenResponse = authServiceGateway.generateToken(user.getId());
        if (StringUtils.isBlank(tokenResponse.getAccessToken())) {
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(), "令牌签发失败");
        }
        return buildUserVO(user, tokenResponse);
    }

    @Override
    public void sendLoginCode(String method, String target) {
        OperationResult result = authServiceGateway.sendLoginCode(method, target);
        if (!result.getSuccess()) {
            log.warn("send login code rejected, method={}, target={}, message={}", method, target, result.getMessage());
            throw new AppException(ResponseCode.CODE_SEND_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.CODE_SEND_FAILED));
        }
    }

    @Override
    public UserVO loginByCode(String method, String target, String code) {
        LoginByCodeRpcResult result = authServiceGateway.loginByCode(method, target, code);
        if (!result.getSuccess() || !result.hasToken() || StringUtils.isBlank(result.getToken().getAccessToken())) {
            log.warn("code login rejected, method={}, target={}, message={}", method, target, result.getMessage());
            throw new AppException(ResponseCode.CODE_LOGIN_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.CODE_LOGIN_FAILED));
        }
        return buildUserVO(result.getUser(), result.getToken());
    }

    @Override
    public String buildOAuthAuthorizeUrl(String provider) {
        OAuthUrlRpcResponse response = authServiceGateway.buildOAuthAuthorizeUrl(provider);
        if (StringUtils.isBlank(response.getUrl())) {
            throw new AppException(ResponseCode.OAUTH_LOGIN_FAILED.getCode(),
                    "该登录方式未启用或未配置凭证: " + provider);
        }
        return response.getUrl();
    }

    @Override
    public UserVO handleOAuthCallback(String provider, String code, String state) {
        OAuthCallbackRpcResult result = authServiceGateway.handleOAuthCallback(provider, code, state);
        if (result.getLogin() && result.hasToken() && StringUtils.isNotBlank(result.getToken().getAccessToken())) {
            return buildUserVO(result.getUser(), result.getToken());
        }
        log.warn("oauth callback rejected, provider={}, login={}, success={}, message={}",
                provider, result.getLogin(), result.getSuccess(), result.getMessage());
        throw new AppException(ResponseCode.OAUTH_LOGIN_FAILED.getCode(),
                resolveMessage(result.getMessage(), ResponseCode.OAUTH_LOGIN_FAILED));
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        TokenRpcResponse tokenResponse = authServiceGateway.refreshToken(refreshToken);
        if (StringUtils.isBlank(tokenResponse.getAccessToken())) {
            throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
        }

        // 解析新 accessToken 补齐用户身份，保持响应结构与登录一致
        TokenValidationResult validation = authServiceGateway.parseToken(tokenResponse.getAccessToken());
        if (!validation.getValid()) {
            throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
        }

        UserVO userVO = new UserVO();
        userVO.setId(validation.getUserId());
        userVO.setUsername(validation.getUsername());
        userVO.setRoles(validation.getRolesList());
        userVO.setPermissions(validation.getPermissionsList());
        userVO.setAccessToken(tokenResponse.getAccessToken());
        userVO.setRefreshToken(tokenResponse.getRefreshToken());
        userVO.setExpiresIn(tokenResponse.getExpiresIn());
        return userVO;
    }

    @Override
    public UserVO getUserById(Long userId) {
        UserRpcResponse user = authServiceGateway.getUserById(userId);
        if (user.getId() <= 0) {
            throw new AppException(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setAvatar(user.getAvatar());
        userVO.setRoles(user.getRolesList());
        userVO.setPermissions(user.getPermissionsList());
        return userVO;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        try {
            authServiceGateway.logout(accessToken, refreshToken);
        } catch (AppException e) {
            // 登出尽力而为：认证服务短暂不可用不应阻塞用户退出本地会话
            log.error("logout rpc failed, fallback to local logout", e);
        }
    }

    private UserVO buildUserVO(UserRpcResponse user, TokenRpcResponse tokenResponse) {
        UserVO userVO = new UserVO();
        if (user != null && user.getId() > 0) {
            userVO.setId(user.getId());
            userVO.setUsername(StringUtils.defaultIfBlank(user.getUsername(), user.getEmail()));
            userVO.setNickname(user.getNickname());
            userVO.setAvatar(user.getAvatar());
            userVO.setRoles(user.getRolesList());
            userVO.setPermissions(user.getPermissionsList());
        }
        userVO.setAccessToken(tokenResponse.getAccessToken());
        userVO.setRefreshToken(tokenResponse.getRefreshToken());
        userVO.setExpiresIn(tokenResponse.getExpiresIn());
        return userVO;
    }

    private String resolveMessage(String upstreamMessage, ResponseCode fallback) {
        return StringUtils.isBlank(upstreamMessage) ? fallback.getMessage() : upstreamMessage;
    }
}
