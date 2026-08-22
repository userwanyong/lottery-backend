package com.lottery.infrastructure.adapter.repository;

import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthUrlRpcResponse;
import cn.wanyj.auth.api.protobuf.OperationResult;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import cn.wanyj.auth.api.protobuf.UpdateUserRpcRequest;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.OAuthBindingVO;
import com.lottery.domain.user.model.vo.OAuthCallbackResultVO;
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
import java.util.stream.Collectors;

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
    public OAuthCallbackResultVO handleOAuthCallback(String provider, String code, String state) {
        OAuthCallbackRpcResult result = authServiceGateway.handleOAuthCallback(provider, code, state);
        if (result.getLogin()) {
            if (!result.hasToken() || StringUtils.isBlank(result.getToken().getAccessToken())) {
                log.warn("oauth login rejected, provider={}, message={}", provider, result.getMessage());
                throw new AppException(ResponseCode.OAUTH_LOGIN_FAILED.getCode(),
                        resolveMessage(result.getMessage(), ResponseCode.OAUTH_LOGIN_FAILED));
            }
            OAuthCallbackResultVO vo = new OAuthCallbackResultVO();
            vo.setLogin(true);
            vo.setUser(buildUserVO(result.getUser(), result.getToken()));
            return vo;
        }

        // 绑定流程（个人中心发起）
        OAuthCallbackResultVO vo = new OAuthCallbackResultVO();
        vo.setLogin(false);
        vo.setBindSuccess(result.getSuccess());
        vo.setMessage(result.getMessage());
        if (!result.getSuccess()) {
            log.warn("oauth bind rejected, provider={}, message={}", provider, result.getMessage());
        }
        return vo;
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

    // ==================== 个人中心 ====================

    @Override
    public AuthUserVO getProfile(Long userId) {
        UserRpcResponse user = authServiceGateway.getUserById(userId);
        if (user.getId() <= 0) {
            throw new AppException(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
        }
        AuthUserVO profile = new AuthUserVO();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setPhone(user.getPhone());
        profile.setNickname(user.getNickname());
        profile.setAvatar(user.getAvatar());
        profile.setStatus(user.getStatus());
        profile.setRoles(user.getRolesList());
        profile.setPermissions(user.getPermissionsList());
        profile.setRealName(user.getRealName());
        profile.setGender(user.getGender());
        profile.setBirthday(user.getBirthday());
        profile.setEmailVerified(user.getEmailVerified());
        profile.setPhoneVerified(user.getPhoneVerified());
        profile.setCreatedAt(user.getCreatedAt());
        profile.setLastLoginAt(user.getLastLoginAt());
        return profile;
    }

    @Override
    public void updateProfile(Long userId, AuthUserUpdateVO updateVO) {
        UpdateUserRpcRequest.Builder builder = UpdateUserRpcRequest.newBuilder();
        List<String> fields = updateVO.buildFieldsToUpdate();
        if (updateVO.getNickname() != null) {
            builder.setNickname(updateVO.getNickname());
        }
        if (updateVO.getAvatar() != null) {
            builder.setAvatar(updateVO.getAvatar());
        }
        if (updateVO.getEmail() != null) {
            builder.setEmail(updateVO.getEmail());
        }
        if (updateVO.getPhone() != null) {
            builder.setPhone(updateVO.getPhone());
        }
        if (updateVO.getRealName() != null) {
            builder.setRealName(updateVO.getRealName());
        }
        if (updateVO.getGender() != null) {
            builder.setGender(updateVO.getGender());
        }
        if (updateVO.getBirthday() != null) {
            builder.setBirthday(updateVO.getBirthday());
        }
        fields.forEach(builder::addFieldsToUpdate);

        OperationResult result = authServiceGateway.updateUser(userId, builder);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        OperationResult result = authServiceGateway.changePassword(userId, oldPassword, newPassword);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public String uploadAvatar(Long userId, String filename, String contentType, byte[] data) {
        String url = authServiceGateway.uploadAvatar(userId, filename, contentType, data);
        if (StringUtils.isBlank(url)) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(), "头像上传失败（存储服务未配置或格式不支持）");
        }
        return url;
    }

    @Override
    public List<OAuthBindingVO> listOAuthBindings(Long userId) {
        return authServiceGateway.listOAuthBindings(userId).stream().map(binding -> {
            OAuthBindingVO vo = new OAuthBindingVO();
            vo.setId(binding.getId());
            vo.setProvider(binding.getProvider());
            vo.setProviderUid(binding.getProviderUid());
            vo.setCreatedAt(binding.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public String buildBindAuthorizeUrl(Long userId, String provider) {
        String url = authServiceGateway.buildBindAuthorizeUrl(userId, provider);
        if (StringUtils.isBlank(url)) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    "该登录方式未启用或未配置凭证: " + provider);
        }
        return url;
    }

    @Override
    public void unbindOAuth(Long userId, String provider) {
        OperationResult result = authServiceGateway.unbindOAuth(userId, provider);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public void bindEmail(Long userId, String method, String target, String code) {
        OperationResult result = authServiceGateway.bindEmail(userId, method, target, code);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public void unbindEmail(Long userId) {
        OperationResult result = authServiceGateway.unbindEmail(userId);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public void bindPhone(Long userId, String method, String target, String code) {
        OperationResult result = authServiceGateway.bindPhone(userId, method, target, code);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
        }
    }

    @Override
    public void unbindPhone(Long userId) {
        OperationResult result = authServiceGateway.unbindPhone(userId);
        if (!result.getSuccess()) {
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(),
                    resolveMessage(result.getMessage(), ResponseCode.AUTH_MANAGE_FAILED));
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
