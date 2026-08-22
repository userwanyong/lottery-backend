package com.lottery.domain.auth.service.impl;

import com.lottery.domain.auth.model.vo.AuthPageVO;
import com.lottery.domain.auth.model.vo.AuthPermissionVO;
import com.lottery.domain.auth.model.vo.AuthRoleVO;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.LoginMethodConfigVO;
import com.lottery.domain.auth.repository.IAuthAdminRepository;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IAuthAdminServiceImpl implements IAuthAdminService {

    @Resource
    private IAuthAdminRepository repository;

    @Override
    public AuthPageVO<AuthUserVO> searchUsers(String keyword, Integer page, Integer size) {
        return repository.searchUsers(keyword, page, size);
    }

    @Override
    public AuthUserVO getUserDetail(Long userId) {
        requireUserId(userId);
        return repository.getUserDetail(userId);
    }

    @Override
    public void updateUser(Long userId, AuthUserUpdateVO updateVO) {
        requireUserId(userId);
        if (updateVO == null || updateVO.buildFieldsToUpdate().isEmpty()) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "未指定任何需要更新的字段");
        }
        if (updateVO.getPassword() != null && updateVO.getPassword().length() < 6) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "密码至少 6 位");
        }
        repository.updateUser(userId, updateVO);
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        requireUserId(userId);
        if (status == null || (status != 0 && status != 1)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "status 仅允许 0 或 1");
        }
        repository.updateUserStatus(userId, status);
    }

    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        requireUserId(userId);
        repository.assignRoles(userId, roleIds);
    }

    @Override
    public void deleteUser(Long userId) {
        requireUserId(userId);
        repository.deleteUser(userId);
    }

    @Override
    public List<AuthRoleVO> listRoles() {
        return repository.listRoles();
    }

    @Override
    public AuthRoleVO createRole(String code, String name, String description) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "角色编码与名称不能为空");
        }
        return repository.createRole(code.trim(), name.trim(), description);
    }

    @Override
    public void updateRole(Long roleId, String name, String description) {
        if (roleId == null || roleId <= 0 || StringUtils.isBlank(name)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "roleId 与角色名称不能为空");
        }
        repository.updateRole(roleId, name.trim(), description);
    }

    @Override
    public void deleteRole(Long roleId) {
        if (roleId == null || roleId <= 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "roleId 不能为空");
        }
        repository.deleteRole(roleId);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null || roleId <= 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "roleId 不能为空");
        }
        repository.assignPermissions(roleId, permissionIds);
    }

    @Override
    public List<AuthPermissionVO> listPermissions() {
        return repository.listPermissions();
    }

    @Override
    public AuthPermissionVO createPermission(String code, String name, String resource, String action, String description) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "权限编码与名称不能为空");
        }
        return repository.createPermission(code.trim(), name.trim(), resource, action, description);
    }

    @Override
    public void deletePermission(Long permissionId) {
        if (permissionId == null || permissionId <= 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "permissionId 不能为空");
        }
        repository.deletePermission(permissionId);
    }

    @Override
    public List<LoginMethodConfigVO> listLoginMethodConfigs() {
        return repository.listLoginMethodConfigs();
    }

    @Override
    public void saveLoginMethodConfig(String method, Integer enabled, Integer usePlatformConfig, String configJson) {
        if (StringUtils.isBlank(method)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "method 不能为空");
        }
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "enabled 仅允许 0 或 1");
        }
        if (usePlatformConfig == null || (usePlatformConfig != 0 && usePlatformConfig != 1)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "usePlatformConfig 仅允许 0 或 1");
        }
        repository.saveLoginMethodConfig(method.trim(), enabled, usePlatformConfig, configJson);
    }

    private void requireUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "userId 不能为空");
        }
    }
}
