package com.lottery.infrastructure.adapter.rpc;

import cn.wanyj.auth.api.protobuf.AssignPermissionsRpcRequest;
import cn.wanyj.auth.api.protobuf.AssignRolesRpcRequest;
import cn.wanyj.auth.api.protobuf.AuthResult;
import cn.wanyj.auth.api.protobuf.AuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.BindContactRpcRequest;
import cn.wanyj.auth.api.protobuf.ChangePasswordRpcRequest;
import cn.wanyj.auth.api.protobuf.ContactBindingRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.ContactUnbindRpcRequest;
import cn.wanyj.auth.api.protobuf.CreatePermissionRpcRequest;
import cn.wanyj.auth.api.protobuf.CreateRoleRpcRequest;
import cn.wanyj.auth.api.protobuf.DeletePermissionRpcRequest;
import cn.wanyj.auth.api.protobuf.DeleteRoleRpcRequest;
import cn.wanyj.auth.api.protobuf.DeleteUserRpcRequest;
import cn.wanyj.auth.api.protobuf.EnabledLoginMethodsRpcRequest;
import cn.wanyj.auth.api.protobuf.GetAllPermissionsRequest;
import cn.wanyj.auth.api.protobuf.GetAllRolesRequest;
import cn.wanyj.auth.api.protobuf.GetRoleByIdRequest;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcRequest;
import cn.wanyj.auth.api.protobuf.LoginByCodeRpcResult;
import cn.wanyj.auth.api.protobuf.LoginMethodListRpcResponse;
import cn.wanyj.auth.api.protobuf.LoginMethodRpcResponse;
import cn.wanyj.auth.api.protobuf.LoginMethodRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.LoginRpcRequest;
import cn.wanyj.auth.api.protobuf.LogoutRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthAuthorizeUrlRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthBindingListRpcResponse;
import cn.wanyj.auth.api.protobuf.OAuthBindingsRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthBindUrlRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthCallbackRpcResult;
import cn.wanyj.auth.api.protobuf.OAuthRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.OAuthUnbindRpcRequest;
import cn.wanyj.auth.api.protobuf.OAuthUrlRpcResponse;
import cn.wanyj.auth.api.protobuf.OperationResult;
import cn.wanyj.auth.api.protobuf.ParseTokenRpcRequest;
import cn.wanyj.auth.api.protobuf.PermissionListResponse;
import cn.wanyj.auth.api.protobuf.PermissionRpcResponse;
import cn.wanyj.auth.api.protobuf.PermissionRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.RefreshTokenRpcRequest;
import cn.wanyj.auth.api.protobuf.RoleListResponse;
import cn.wanyj.auth.api.protobuf.RoleRpcResponse;
import cn.wanyj.auth.api.protobuf.RoleRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.SaveTenantLoginMethodRpcRequest;
import cn.wanyj.auth.api.protobuf.SearchUsersRequest;
import cn.wanyj.auth.api.protobuf.SendCodeRpcRequest;
import cn.wanyj.auth.api.protobuf.StringListResponse;
import cn.wanyj.auth.api.protobuf.TenantLoginMethodRpcRequest;
import cn.wanyj.auth.api.protobuf.TokenGenerationRequest;
import cn.wanyj.auth.api.protobuf.TokenRpcResponse;
import cn.wanyj.auth.api.protobuf.TokenRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import cn.wanyj.auth.api.protobuf.UpdateRoleRpcRequest;
import cn.wanyj.auth.api.protobuf.UpdateUserRpcRequest;
import cn.wanyj.auth.api.protobuf.UpdateUserStatusRpcRequest;
import cn.wanyj.auth.api.protobuf.UploadAvatarRpcRequest;
import cn.wanyj.auth.api.protobuf.UploadAvatarRpcResponse;
import cn.wanyj.auth.api.protobuf.OssRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.UserByIdRequest;
import cn.wanyj.auth.api.protobuf.UserPageResponse;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import cn.wanyj.auth.api.protobuf.UserRpcServiceProtobuf;
import com.google.protobuf.ByteString;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * auth-service RPC 统一网关：集中设置 rpc-service-token attachment、按读写语义配置重试并兜底网络异常。
 * 调用约定见 auth-service docs/ai-rpc-integration.md：业务失败不抛异常，按响应字段判断；ID 一律数字字符串。
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
     * 写语义（登录、发码、令牌轮换、登出、改密）必须 retries=0，避免重试导致验证码重复消费/令牌对轮换错乱
     */
    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private AuthRpcServiceProtobuf authRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private OAuthRpcServiceProtobuf oauthRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private UserRpcServiceProtobuf userRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private RoleRpcServiceProtobuf roleRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private PermissionRpcServiceProtobuf permissionRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private ContactBindingRpcServiceProtobuf contactBindingRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 0)
    private OssRpcServiceProtobuf ossRpcService;

    /**
     * 读语义（解析令牌）允许一次重试
     */
    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 1)
    private TokenRpcServiceProtobuf tokenRpcService;

    @DubboReference(version = "1.0.0", check = false, timeout = 15000, retries = 1)
    private LoginMethodRpcServiceProtobuf loginMethodRpcService;

    // ==================== 登录与令牌 ====================

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
            throw rpcFailure("list enabled login methods", e);
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
            throw rpcFailure("authenticate", e);
        }
    }

    public UserRpcResponse getUserById(Long userId) {
        try {
            attachRpcToken();
            UserRpcResponse response = authRpcService.getUserById(UserByIdRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
            return nullSafe(response, UserRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("get user by id", e);
        }
    }

    public TokenRpcResponse generateToken(Long userId) {
        try {
            attachRpcToken();
            TokenRpcResponse response = tokenRpcService.generateToken(TokenGenerationRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
            return nullSafe(response, TokenRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("generate token", e);
        }
    }

    public TokenValidationResult parseToken(String accessToken) {
        try {
            attachRpcToken();
            TokenValidationResult response = tokenRpcService.parseToken(
                    ParseTokenRpcRequest.newBuilder().setAccessToken(accessToken).build());
            return nullSafe(response, TokenValidationResult.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("parse token", e);
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
            throw rpcFailure("send login code", e);
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
            return nullSafe(response, LoginByCodeRpcResult.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("login by code", e);
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
            return nullSafe(response, OAuthUrlRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("build oauth authorize url", e);
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
            return nullSafe(response, OAuthCallbackRpcResult.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("handle oauth callback", e);
        }
    }

    public TokenRpcResponse refreshToken(String refreshToken) {
        try {
            attachRpcToken();
            TokenRpcResponse response = authRpcService.refreshToken(
                    RefreshTokenRpcRequest.newBuilder().setRefreshToken(refreshToken).build());
            return nullSafe(response, TokenRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("refresh token", e);
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
            throw rpcFailure("logout", e);
        }
    }

    // ==================== 个人中心 ====================

    /**
     * 修改自己的密码（不撤销已有令牌）
     */
    public OperationResult changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            attachRpcToken();
            return authRpcService.changePassword(ChangePasswordRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .setOldPassword(oldPassword)
                    .setNewPassword(newPassword)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("change password", e);
        }
    }

    /**
     * 头像上传（auth-service 转存其配置的对象存储）
     */
    public String uploadAvatar(Long userId, String filename, String contentType, byte[] data) {
        try {
            attachRpcToken();
            UploadAvatarRpcResponse response = ossRpcService.uploadAvatar(UploadAvatarRpcRequest.newBuilder()
                    .setTenantUid(tenantUid)
                    .setUserId(String.valueOf(userId))
                    .setFilename(filename)
                    .setContentType(contentType)
                    .setData(ByteString.copyFrom(data))
                    .build());
            return response == null ? "" : response.getUrl();
        } catch (RpcException e) {
            throw rpcFailure("upload avatar", e);
        }
    }

    public List<cn.wanyj.auth.api.protobuf.OAuthBindingRpcResponse> listOAuthBindings(Long userId) {
        try {
            attachRpcToken();
            OAuthBindingListRpcResponse response = oauthRpcService.listBindings(
                    OAuthBindingsRpcRequest.newBuilder()
                            .setTenantUid(tenantUid)
                            .setUserId(String.valueOf(userId))
                            .build());
            return response == null ? Collections.emptyList() : response.getBindingsList();
        } catch (RpcException e) {
            throw rpcFailure("list oauth bindings", e);
        }
    }

    public String buildBindAuthorizeUrl(Long userId, String provider) {
        try {
            attachRpcToken();
            OAuthUrlRpcResponse response = oauthRpcService.buildBindAuthorizeUrl(
                    OAuthBindUrlRpcRequest.newBuilder()
                            .setTenantUid(tenantUid)
                            .setUserId(String.valueOf(userId))
                            .setProvider(provider)
                            .build());
            return response == null ? "" : response.getUrl();
        } catch (RpcException e) {
            throw rpcFailure("build bind authorize url", e);
        }
    }

    public OperationResult unbindOAuth(Long userId, String provider) {
        try {
            attachRpcToken();
            return oauthRpcService.unbind(OAuthUnbindRpcRequest.newBuilder()
                    .setTenantUid(tenantUid)
                    .setUserId(String.valueOf(userId))
                    .setProvider(provider)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("unbind oauth", e);
        }
    }

    public OperationResult bindEmail(Long userId, String method, String target, String code) {
        try {
            attachRpcToken();
            return contactBindingRpcService.bindEmail(BindContactRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .setMethod(method)
                    .setTarget(target)
                    .setCode(code)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("bind email", e);
        }
    }

    public OperationResult unbindEmail(Long userId) {
        try {
            attachRpcToken();
            return contactBindingRpcService.unbindEmail(ContactUnbindRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("unbind email", e);
        }
    }

    public OperationResult bindPhone(Long userId, String method, String target, String code) {
        try {
            attachRpcToken();
            return contactBindingRpcService.bindPhone(BindContactRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .setMethod(method)
                    .setTarget(target)
                    .setCode(code)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("bind phone", e);
        }
    }

    public OperationResult unbindPhone(Long userId) {
        try {
            attachRpcToken();
            return contactBindingRpcService.unbindPhone(ContactUnbindRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("unbind phone", e);
        }
    }

    // ==================== 用户管理 ====================

    public UserPageResponse searchUsers(String keyword, int page, int size) {
        try {
            attachRpcToken();
            UserPageResponse response = authRpcService.searchUsers(SearchUsersRequest.newBuilder()
                    .setKeyword(keyword == null ? "" : keyword)
                    .setTenantUid(tenantUid)
                    .setPage(page <= 0 ? 1 : page)
                    .setSize(size <= 0 ? 10 : size)
                    .build());
            return nullSafe(response, UserPageResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("search users", e);
        }
    }

    public OperationResult updateUser(Long userId, UpdateUserRpcRequest.Builder builder) {
        try {
            attachRpcToken();
            return userRpcService.updateUser(builder
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("update user", e);
        }
    }

    public OperationResult updateUserStatus(Long userId, int status) {
        try {
            attachRpcToken();
            return userRpcService.updateUserStatus(UpdateUserStatusRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .setStatus(status)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("update user status", e);
        }
    }

    public OperationResult assignRoles(Long userId, List<String> roleIds) {
        try {
            attachRpcToken();
            AssignRolesRpcRequest.Builder builder = AssignRolesRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid);
            roleIds.forEach(builder::addRoleIds);
            return userRpcService.assignRoles(builder.build());
        } catch (RpcException e) {
            throw rpcFailure("assign roles", e);
        }
    }

    public OperationResult deleteUser(Long userId) {
        try {
            attachRpcToken();
            return userRpcService.deleteUser(DeleteUserRpcRequest.newBuilder()
                    .setUserId(String.valueOf(userId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("delete user", e);
        }
    }

    // ==================== 角色管理 ====================

    public List<RoleRpcResponse> listRoles() {
        try {
            attachRpcToken();
            RoleListResponse response = roleRpcService.getAllRoles(
                    GetAllRolesRequest.newBuilder().setTenantUid(tenantUid).build());
            return response == null ? Collections.emptyList() : response.getRolesList();
        } catch (RpcException e) {
            throw rpcFailure("list roles", e);
        }
    }

    public RoleRpcResponse getRoleById(Long roleId) {
        try {
            attachRpcToken();
            RoleRpcResponse response = roleRpcService.getRoleById(GetRoleByIdRequest.newBuilder()
                    .setRoleId(String.valueOf(roleId))
                    .setTenantUid(tenantUid)
                    .build());
            return nullSafe(response, RoleRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("get role by id", e);
        }
    }

    public RoleRpcResponse createRole(String code, String name, String description) {
        try {
            attachRpcToken();
            RoleRpcResponse response = roleRpcService.createRole(CreateRoleRpcRequest.newBuilder()
                    .setCode(code)
                    .setName(name)
                    .setDescription(description == null ? "" : description)
                    .setTenantUid(tenantUid)
                    .build());
            return nullSafe(response, RoleRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("create role", e);
        }
    }

    public OperationResult updateRole(Long roleId, String name, String description) {
        try {
            attachRpcToken();
            return roleRpcService.updateRole(UpdateRoleRpcRequest.newBuilder()
                    .setRoleId(String.valueOf(roleId))
                    .setName(name)
                    .setDescription(description == null ? "" : description)
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("update role", e);
        }
    }

    public OperationResult deleteRole(Long roleId) {
        try {
            attachRpcToken();
            return roleRpcService.deleteRole(DeleteRoleRpcRequest.newBuilder()
                    .setRoleId(String.valueOf(roleId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("delete role", e);
        }
    }

    public OperationResult assignPermissions(Long roleId, List<String> permissionIds) {
        try {
            attachRpcToken();
            AssignPermissionsRpcRequest.Builder builder = AssignPermissionsRpcRequest.newBuilder()
                    .setRoleId(String.valueOf(roleId))
                    .setTenantUid(tenantUid);
            permissionIds.forEach(builder::addPermissionIds);
            return roleRpcService.assignPermissions(builder.build());
        } catch (RpcException e) {
            throw rpcFailure("assign permissions", e);
        }
    }

    // ==================== 权限管理 ====================

    public List<PermissionRpcResponse> listPermissions() {
        try {
            attachRpcToken();
            PermissionListResponse response = permissionRpcService.getAllPermissions(
                    GetAllPermissionsRequest.newBuilder().setTenantUid(tenantUid).build());
            return response == null ? Collections.emptyList() : response.getPermissionsList();
        } catch (RpcException e) {
            throw rpcFailure("list permissions", e);
        }
    }

    public PermissionRpcResponse createPermission(String code, String name, String resource, String action, String description) {
        try {
            attachRpcToken();
            PermissionRpcResponse response = permissionRpcService.createPermission(
                    CreatePermissionRpcRequest.newBuilder()
                            .setCode(code)
                            .setName(name)
                            .setResource(resource == null ? "" : resource)
                            .setAction(action == null ? "" : action)
                            .setDescription(description == null ? "" : description)
                            .setTenantUid(tenantUid)
                            .build());
            return nullSafe(response, PermissionRpcResponse.getDefaultInstance());
        } catch (RpcException e) {
            throw rpcFailure("create permission", e);
        }
    }

    public OperationResult deletePermission(Long permissionId) {
        try {
            attachRpcToken();
            return permissionRpcService.deletePermission(DeletePermissionRpcRequest.newBuilder()
                    .setPermissionId(String.valueOf(permissionId))
                    .setTenantUid(tenantUid)
                    .build());
        } catch (RpcException e) {
            throw rpcFailure("delete permission", e);
        }
    }

    // ==================== 登录方式管理（租户级） ====================

    public List<LoginMethodRpcResponse> listTenantLoginMethods() {
        try {
            attachRpcToken();
            LoginMethodListRpcResponse response = loginMethodRpcService.listTenantConfigs(
                    TenantLoginMethodRpcRequest.newBuilder().setTenantUid(tenantUid).build());
            return response == null ? Collections.emptyList() : response.getItemsList();
        } catch (RpcException e) {
            throw rpcFailure("list tenant login methods", e);
        }
    }

    public OperationResult saveTenantLoginMethod(String method, int enabled, int usePlatformConfig, String configJson) {
        try {
            attachRpcToken();
            return loginMethodRpcService.saveTenantConfig(
                    SaveTenantLoginMethodRpcRequest.newBuilder()
                            .setTenantUid(tenantUid)
                            .setMethod(method)
                            .setEnabled(enabled)
                            .setUsePlatformConfig(usePlatformConfig)
                            .setConfigJson(configJson == null ? "" : configJson)
                            .build());
        } catch (RpcException e) {
            throw rpcFailure("save tenant login method", e);
        }
    }

    private void attachRpcToken() {
        RpcContext.getClientAttachment().setAttachment(RPC_TOKEN_ATTACHMENT, rpcToken);
    }

    private <T> T nullSafe(T response, T defaultInstance) {
        return response == null ? defaultInstance : response;
    }

    private AppException rpcFailure(String action, RpcException e) {
        log.error("auth-service rpc failed: {}", action, e);
        return new AppException(ResponseCode.AUTH_SERVICE_ERROR.getCode(),
                ResponseCode.AUTH_SERVICE_ERROR.getMessage() + ": " + e.getMessage());
    }
}
