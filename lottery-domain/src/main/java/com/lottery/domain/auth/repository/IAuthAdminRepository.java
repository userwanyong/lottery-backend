package com.lottery.domain.auth.repository;

import com.lottery.domain.auth.model.vo.AuthPageVO;
import com.lottery.domain.auth.model.vo.AuthPermissionVO;
import com.lottery.domain.auth.model.vo.AuthRoleVO;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.LoginMethodConfigVO;

import java.util.List;

/**
 * 认证管理仓储端口：用户/角色/权限/登录方式管理，全部委托 auth-service RPC
 */
public interface IAuthAdminRepository {

    AuthPageVO<AuthUserVO> searchUsers(String keyword, Integer page, Integer size);

    AuthUserVO getUserDetail(Long userId);

    void updateUser(Long userId, AuthUserUpdateVO updateVO);

    void updateUserStatus(Long userId, Integer status);

    void assignRoles(Long userId, List<Long> roleIds);

    void deleteUser(Long userId);

    List<AuthRoleVO> listRoles();

    AuthRoleVO createRole(String code, String name, String description);

    void updateRole(Long roleId, String name, String description);

    void deleteRole(Long roleId);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    List<AuthPermissionVO> listPermissions();

    AuthPermissionVO createPermission(String code, String name, String resource, String action, String description);

    void deletePermission(Long permissionId);

    List<LoginMethodConfigVO> listLoginMethodConfigs();

    void saveLoginMethodConfig(String method, Integer enabled, Integer usePlatformConfig, String configJson);
}
