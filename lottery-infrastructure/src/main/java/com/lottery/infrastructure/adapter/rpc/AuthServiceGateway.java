package com.lottery.infrastructure.adapter.rpc;

import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.AuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.EnabledLoginMethodsRpcRequest;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcRequest;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcResult;
import cn.wanyj.auth.api.protobuf.LoginMethodRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.LoginRpcRequest;
import cn.wanyj.auth.api.protobuf.LogoutRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthAuthorizeUrlRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.OAuthUrlRpcResponse;
import cn.wanyj.auth.api.protobuf.OperationResult;
import cn.wanyj.auth.api.protobuf.ParseTokenRpcRequest;
import cn.wanyj.auth.api.protobuf.RefreshTokenRpcRequest;
import cn.wanyj.auth.api.protobuf.SendCodeRpcRequest;
import cn.wanyj.auth.api.protobuf.StringListResponse;
import cn.wanyj.auth.api.protobuf.TokenGenerationRequest;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import cn.wanyj.auth.api.protobuf.UserByIdRequest;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * auth-service RPC 统一网关：集中设置 rpc-service-token attachment、按读写语义配置重试并兜底网络异常。
 * 调用约定见 auth-service docs/ai-rpc-integration.md：业务失败不抛异常，按响应字段判断。
 */
@Slf4j
@Component
public class AuthServiceGateway {

    private static final String RPC_TOKEN_ATTACHMENT = "rpc-service-token";

    @Value("${auth.tenant-uid:}")
    private String tenantUid;

    @Value("${auth.rpc-token:}")
    private String rpcToken;

    /**
     * 写语义（登录、发码、令牌轮换、登出）必须 retries=0，避免重试导致验证码重复消费/令牌对轮换错乱
     */
    @DubboReference(version = "1.0.0", check = false, timeout = 5000, retries = 0)
    private AuthRpcServiceProtobuf authRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 5000, retries = 0)
    private OAuthRpcServiceProtobuf oauthRpcService;

    /**
     * 读语义（解析令牌）允许一次重试
     */
    @DubboReference(version = "1.0.0", check = false, timeout = 3000, retries = 1)
    private TokenRpcServiceProtobuf tokenRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 5000, retries = 1)
    private LoginMethodRpcServiceProtobuf loginMethodRpcService;

    /**
     * 登录页渲染依据：当前租户启用的登录方式（与 auth-service 管理端开闭实时同步）
     */
    public List<String> listEnabledLoginMethods() {
        try {
            attachRpcToken();
            StringListResponse response = loginMethodRpcService.listEnabledMethods(
                    EnabledLoginMethodsRpcRequest.newBuilder().setTenantUid(tenantUid).build());
            List<String> methods = response.getValuesList();
            return methods == null ? Collections.emptyList() : methods;
        } catch (RpcException e) {
            log.error("list enabled login methods rpc failed", e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    /**
     * 账号密码校验（支持用户名或邮箱），只做校验不签发令牌，令牌由 generateToken 补发
     */
    public AuthResult authenticate(String username, String password) {
        try {
            attachRpcToken();
            return authRpcService.authenticate(LoginRpcRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            log.error("authenticate rpc failed, username={}", username, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public UserRpcResponse getUserById(Long userId) {
        try {
            attachRpcToken();
            return authRpcService.getUserById(UserByIdRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            log.error("get user by id rpc failed, userId={}", userId, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public TokenRpcResponse generateToken(Long userId) {
        try {
            attachRpcToken();
            TokenRpcResponse response = tokenRpcService.generateToken(TokenGenerationRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
            return response == null ? TokenRpcResponse.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("generate token rpc failed, userId={}", userId, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public TokenValidationResult parseToken(String accessToken) {
        try {
            attachRpcToken();
            TokenValidationResult response = tokenRpcService.parseToken(
                    ParseTokenRpcRequest.newBuilder().setAccessToken(accessToken).build());
            return response == null ? TokenValidationResult.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("parse token rpc failed", e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public OperationResult sendLoginCode(String method, String target) {
        try {
            attachRpcToken();
            return authRpcService.sendCode(SendCodeRpcRequest.newBuilder()
                    .setTenantUid(tenantUid)
                    .setMethod(method)
                    .setTarget(target)
                    .build());
        } catch (RpcException e) {
            log.error("send login code rpc failed, method={}, target={}", method, target, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    /**
     * 验证码登录：未知邮箱/手机号由 auth-service 自动注册
     */
    public LoginByCodeRpcResult loginByCode(String method, String target, String code) {
        try {
            attachRpcToken();
            LoginByCodeRpcResult response = authRpcService.loginByCode(LoginByCodeRpcRequest.newBuilder()
                    .setTenantUid(tenantUid)
                    .setMethod(method)
                    .setTarget(target)
                    .setCode(code)
                    .build());
            return response == null ? LoginByCodeRpcResult.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("login by code rpc failed, method={}, target={}", method, target, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public OAuthUrlRpcResponse buildOAuthAuthorizeUrl(String provider) {
        try {
            attachRpcToken();
            OAuthUrlRpcResponse response = oauthRpcService.buildAuthorizeUrl(
                    OAuthAuthorizeUrlRpcRequest.newBuilder()
                            .setTenantUid(tenantUid)
                            .setProvider(provider)
                            .build());
            return response == null ? OAuthUrlRpcResponse.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("build oauth authorize url rpc failed, provider={}", provider, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public OAuthCallbackRpcResult handleOAuthCallback(String provider, String code, String state) {
        try {
            attachRpcToken();
            OAuthCallbackRpcResult response = oauthRpcService.handleCallback(
                    OAuthCallbackRpcRequest.newBuilder()
                            .setProvider(provider)
                            .setCode(code)
                            .setState(state)
                            .build());
            return response == null ? OAuthCallbackRpcResult.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("handle oauth callback rpc failed, provider={}", provider, e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public TokenRpcResponse refreshToken(String refreshToken) {
        try {
            attachRpcToken();
            TokenRpcResponse response = authRpcService.refreshToken(
                    RefreshTokenRpcRequest.newBuilder().setRefreshToken(refreshToken).build());
            return response == null ? TokenRpcResponse.getDefaultInstance() : response;
        } catch (RpcException e) {
            log.error("refresh token rpc failed", e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    public void logout(String accessToken, String refreshToken) {
        try {
            attachRpcToken();
            authRpcService.logout(LogoutRpcRequest.newBuilder()
                    .setAccessToken(accessToken == null ? "" : accessToken)
                    .setRefreshToken(refreshToken == null ? "" : refreshToken)
                    .build());
        } catch (RpcException e) {
            log.error("logout rpc failed", e);
            throw new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                    ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
        }
    }

    private void attachRpcToken() {
        RpcContext.getClientAttachment().setAttachment(RPC_TOKEN_ATTACHMENT, rpcToken);
    }
}
