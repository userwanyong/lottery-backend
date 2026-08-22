package com.lottery.domain.auth.service.impl;

import com.lottery.domain.auth.model.vo.AuthPageVO;
import com.lottery.domain.auth.model.vo.AuthPermissionVO;
import com.lottery.domain.auth.model.vo.AuthRoleVO;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.LoginMethodConfigVO;

import java.util.List;

/**
 * 认证管理服务：用户/角色/权限/登录方式管理（与 auth-service 控制台功能一致，同一数据源）
 */
public interface IAuthAdminService {

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
