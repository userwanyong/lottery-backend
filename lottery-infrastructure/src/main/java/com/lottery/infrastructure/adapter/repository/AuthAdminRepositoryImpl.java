package com.lottery.infrastructure.adapter.repository;

import cn.wanyj.auth.api.protobuf.LoginMethodRpcResponse;
import cn.wanyj.auth.api.protobuf.OperationResult;
import cn.wanyj.auth.api.protobuf.PermissionRpcResponse;
import cn.wanyj.auth.api.protobuf.RoleRpcResponse;
import cn.wanyj.auth.api.protobuf.UpdateUserRpcRequest;
import cn.wanyj.auth.api.protobuf.UserPageResponse;
import cn.wanyj.auth.api.protobuf.UserRpcResponse;
import com.lottery.domain.auth.model.vo.AuthPageVO;
import com.lottery.domain.auth.model.vo.AuthPermissionVO;
import com.lottery.domain.auth.model.vo.AuthRoleVO;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.LoginMethodConfigVO;
import com.lottery.domain.auth.repository.IAuthAdminRepository;
import com.lottery.infrastructure.adapter.rpc.AuthServiceGateway;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证管理仓储实现：用户/角色/权限/登录方式管理全部委托 auth-service RPC，
 * 与 auth-service 自带控制台操作同一数据源，天然同步。
 */
@Slf4j
@Repository
public class AuthAdminRepositoryImpl implements IAuthAdminRepository {

    @Resource
    private AuthServiceGateway gateway;

    @Override
    public AuthPageVO<AuthUserVO> searchUsers(String keyword, Integer page, Integer size) {
        UserPageResponse response = gateway.searchUsers(keyword, page == null ? 1 : page, size == null ? 10 : size);
        AuthPageVO<AuthUserVO> pageVO = new AuthPageVO<>();
        pageVO.setTotal(response.getTotal());
        pageVO.setPage(response.getPage());
        pageVO.setSize(response.getSize());
        pageVO.setItems(response.getItemsList().stream().map(this::toUserVO).collect(Collectors.toList()));
        return pageVO;
    }

    @Override
    public AuthUserVO getUserDetail(Long userId) {
        UserRpcResponse user = gateway.getUserById(userId);
        if (user.getId() <= 0) {
            throw new AppException(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
        }
        return toUserVO(user);
    }

    @Override
    public void updateUser(Long userId, AuthUserUpdateVO updateVO) {
        List<String> fields = updateVO.buildFieldsToUpdate();
        UpdateUserRpcRequest.Builder builder = UpdateUserRpcRequest.newBuilder();
        if (updateVO.getUsername() != null) {
            builder.setUsername(updateVO.getUsername());
        }
        if (updateVO.getPassword() != null) {
            builder.setPassword(updateVO.getPassword());
        }
        if (updateVO.getEmail() != null) {
            builder.setEmail(updateVO.getEmail());
        }
        if (updateVO.getPhone() != null) {
            builder.setPhone(updateVO.getPhone());
        }
        if (updateVO.getNickname() != null) {
            builder.setNickname(updateVO.getNickname());
        }
        if (updateVO.getAvatar() != null) {
            builder.setAvatar(updateVO.getAvatar());
        }
        if (updateVO.getStatus() != null) {
            builder.setStatus(updateVO.getStatus());
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

        ensureSuccess(gateway.updateUser(userId, builder), "更新用户失败");
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        ensureSuccess(gateway.updateUserStatus(userId, status), "更新用户状态失败");
    }

    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        List<String> ids = roleIds == null ? List.of() : roleIds.stream().map(String::valueOf).collect(Collectors.toList());
        ensureSuccess(gateway.assignRoles(userId, ids), "分配角色失败");
    }

    @Override
    public void deleteUser(Long userId) {
        ensureSuccess(gateway.deleteUser(userId), "删除用户失败");
    }

    @Override
    public List<AuthRoleVO> listRoles() {
        return gateway.listRoles().stream().map(this::toRoleVO).collect(Collectors.toList());
    }

    @Override
    public AuthRoleVO createRole(String code, String name, String description) {
        RoleRpcResponse role = gateway.createRole(code, name, description);
        if (role.getId() <= 0) {
            throw new AppException(ResponseCode.DATA_EXIST.getCode(), "角色创建失败：编码可能已存在");
        }
        return toRoleVO(role);
    }

    @Override
    public void updateRole(Long roleId, String name, String description) {
        ensureSuccess(gateway.updateRole(roleId, name, description), "更新角色失败");
    }

    @Override
    public void deleteRole(Long roleId) {
        ensureSuccess(gateway.deleteRole(roleId), "删除角色失败");
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        List<String> ids = permissionIds == null ? List.of() : permissionIds.stream().map(String::valueOf).collect(Collectors.toList());
        ensureSuccess(gateway.assignPermissions(roleId, ids), "分配权限失败");
    }

    @Override
    public List<AuthPermissionVO> listPermissions() {
        return gateway.listPermissions().stream().map(this::toPermissionVO).collect(Collectors.toList());
    }

    @Override
    public AuthPermissionVO createPermission(String code, String name, String resource, String action, String description) {
        PermissionRpcResponse permission = gateway.createPermission(code, name, resource, action, description);
        if (permission.getId() <= 0) {
            throw new AppException(ResponseCode.DATA_EXIST.getCode(), "权限创建失败：编码可能已存在");
        }
        return toPermissionVO(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        ensureSuccess(gateway.deletePermission(permissionId), "删除权限失败");
    }

    @Override
    public List<LoginMethodConfigVO> listLoginMethodConfigs() {
        return gateway.listTenantLoginMethods().stream().map(this::toLoginMethodVO).collect(Collectors.toList());
    }

    @Override
    public void saveLoginMethodConfig(String method, Integer enabled, Integer usePlatformConfig, String configJson) {
        ensureSuccess(gateway.saveTenantLoginMethod(method, enabled, usePlatformConfig, configJson), "保存登录方式配置失败");
    }

    private AuthUserVO toUserVO(UserRpcResponse user) {
        AuthUserVO vo = new AuthUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setRoles(user.getRolesList());
        vo.setPermissions(user.getPermissionsList());
        vo.setRealName(user.getRealName());
        vo.setGender(user.getGender());
        vo.setBirthday(user.getBirthday());
        vo.setEmailVerified(user.getEmailVerified());
        vo.setPhoneVerified(user.getPhoneVerified());
        vo.setCreatedAt(user.getCreatedAt());
        vo.setLastLoginAt(user.getLastLoginAt());
        return vo;
    }

    private AuthRoleVO toRoleVO(RoleRpcResponse role) {
        AuthRoleVO vo = new AuthRoleVO();
        vo.setId(role.getId());
        vo.setCode(role.getCode());
        vo.setName(role.getName());
        vo.setDescription(role.getDescription());
        vo.setStatus(role.getStatus());
        vo.setPermissions(role.getPermissionsList());
        return vo;
    }

    private AuthPermissionVO toPermissionVO(PermissionRpcResponse permission) {
        AuthPermissionVO vo = new AuthPermissionVO();
        vo.setId(permission.getId());
        vo.setCode(permission.getCode());
        vo.setName(permission.getName());
        vo.setResource(permission.getResource());
        vo.setAction(permission.getAction());
        vo.setDescription(permission.getDescription());
        return vo;
    }

    private LoginMethodConfigVO toLoginMethodVO(LoginMethodRpcResponse config) {
        LoginMethodConfigVO vo = new LoginMethodConfigVO();
        vo.setMethod(config.getMethod());
        vo.setCategory(config.getCategory());
        vo.setDisplayName(config.getDisplayName());
        vo.setEnabled(config.getEnabled());
        vo.setUsePlatformConfig(config.getUsePlatformConfig());
        vo.setHasConfig(config.getHasConfig());
        vo.setPlatformEnabled(config.getPlatformEnabled());
        return vo;
    }

    private void ensureSuccess(OperationResult result, String fallback) {
        if (result == null || !result.getSuccess()) {
            String message = result == null || StringUtils.isBlank(result.getMessage()) ? fallback : result.getMessage();
            log.warn("auth admin operation rejected: {}", message);
            throw new AppException(ResponseCode.AUTH_MANAGE_FAILED.getCode(), message);
        }
    }
}
