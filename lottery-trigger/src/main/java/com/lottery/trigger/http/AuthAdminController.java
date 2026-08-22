package com.lottery.trigger.http;

import com.lottery.domain.auth.model.vo.AuthPageVO;
import com.lottery.domain.auth.model.vo.AuthPermissionVO;
import com.lottery.domain.auth.model.vo.AuthRoleVO;
import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.LoginMethodConfigVO;
import com.lottery.domain.auth.service.impl.IAuthAdminService;
import com.lottery.trigger.api.dto.req.AuthAssignPermissionsRequestDTO;
import com.lottery.trigger.api.dto.req.AuthAssignRolesRequestDTO;
import com.lottery.trigger.api.dto.req.AuthLoginMethodSaveRequestDTO;
import com.lottery.trigger.api.dto.req.AuthPermissionRequestDTO;
import com.lottery.trigger.api.dto.req.AuthRoleRequestDTO;
import com.lottery.trigger.api.dto.req.AuthUserStatusRequestDTO;
import com.lottery.trigger.api.dto.req.AuthUserUpdateRequestDTO;
import com.lottery.trigger.api.dto.res.AuthLoginMethodDTO;
import com.lottery.trigger.api.dto.res.AuthPageDTO;
import com.lottery.trigger.api.dto.res.AuthPermissionDTO;
import com.lottery.trigger.api.dto.res.AuthRoleDTO;
import com.lottery.trigger.api.dto.res.AuthUserDTO;
import com.lottery.types.annotation.PermissionCheck;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证管理：用户/角色/权限/登录方式管理，全部经 RPC 委托 auth-service，
 * 与其自带控制台操作同一数据源（实时同步）。仅 ROLE_ADMIN 可操作。
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Slf4j
public class AuthAdminController {

    @Resource
    private IAuthAdminService authAdminService;

    // ==================== 用户管理 ====================

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @GetMapping("/users")
    public BaseResponse<AuthPageDTO<AuthUserDTO>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        AuthPageVO<AuthUserVO> pageVO = authAdminService.searchUsers(keyword, page, size);
        AuthPageDTO<AuthUserDTO> dto = new AuthPageDTO<>();
        dto.setTotal(pageVO.getTotal());
        dto.setPage(pageVO.getPage());
        dto.setSize(pageVO.getSize());
        dto.setItems(pageVO.getItems().stream().map(this::toUserDTO).collect(Collectors.toList()));
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), dto);
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @GetMapping("/users/{userId}")
    public BaseResponse<AuthUserDTO> getUserDetail(@PathVariable Long userId) {
        return success(toUserDTO(authAdminService.getUserDetail(userId)));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/users/{userId}")
    public BaseResponse<Void> updateUser(@PathVariable Long userId, @RequestBody AuthUserUpdateRequestDTO request) {
        authAdminService.updateUser(userId, toUpdateVO(request));
        return success();
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/users/{userId}/status")
    public BaseResponse<Void> updateUserStatus(@PathVariable Long userId,
                                               @Valid @RequestBody AuthUserStatusRequestDTO request) {
        authAdminService.updateUserStatus(userId, request.getStatus());
        return success();
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/users/{userId}/roles")
    public BaseResponse<Void> assignRoles(@PathVariable Long userId,
                                          @RequestBody AuthAssignRolesRequestDTO request) {
        authAdminService.assignRoles(userId, request.getRoleIds());
        return success();
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteMapping("/users/{userId}")
    public BaseResponse<Void> deleteUser(@PathVariable Long userId) {
        authAdminService.deleteUser(userId);
        return success();
    }

    // ==================== 角色管理 ====================

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @GetMapping("/roles")
    public BaseResponse<List<AuthRoleDTO>> listRoles() {
        return success(authAdminService.listRoles().stream().map(this::toRoleDTO).collect(Collectors.toList()));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PostMapping("/roles")
    public BaseResponse<AuthRoleDTO> createRole(@Valid @RequestBody AuthRoleRequestDTO request) {
        return success(toRoleDTO(authAdminService.createRole(request.getCode(), request.getName(), request.getDescription())));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/roles/{roleId}")
    public BaseResponse<Void> updateRole(@PathVariable Long roleId, @Valid @RequestBody AuthRoleRequestDTO request) {
        authAdminService.updateRole(roleId, request.getName(), request.getDescription());
        return success();
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteMapping("/roles/{roleId}")
    public BaseResponse<Void> deleteRole(@PathVariable Long roleId) {
        authAdminService.deleteRole(roleId);
        return success();
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/roles/{roleId}/permissions")
    public BaseResponse<Void> assignPermissions(@PathVariable Long roleId,
                                                @RequestBody AuthAssignPermissionsRequestDTO request) {
        authAdminService.assignPermissions(roleId, request.getPermissionIds());
        return success();
    }

    // ==================== 权限管理 ====================

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @GetMapping("/permissions")
    public BaseResponse<List<AuthPermissionDTO>> listPermissions() {
        return success(authAdminService.listPermissions().stream().map(this::toPermissionDTO).collect(Collectors.toList()));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PostMapping("/permissions")
    public BaseResponse<AuthPermissionDTO> createPermission(@Valid @RequestBody AuthPermissionRequestDTO request) {
        return success(toPermissionDTO(authAdminService.createPermission(request.getCode(), request.getName(),
                request.getResource(), request.getAction(), request.getDescription())));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteMapping("/permissions/{permissionId}")
    public BaseResponse<Void> deletePermission(@PathVariable Long permissionId) {
        authAdminService.deletePermission(permissionId);
        return success();
    }

    // ==================== 登录方式管理 ====================

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @GetMapping("/login-methods")
    public BaseResponse<List<AuthLoginMethodDTO>> listLoginMethods() {
        return success(authAdminService.listLoginMethodConfigs().stream()
                .map(this::toLoginMethodDTO).collect(Collectors.toList()));
    }

    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @PutMapping("/login-methods/{method}")
    public BaseResponse<Void> saveLoginMethod(@PathVariable String method,
                                              @Valid @RequestBody AuthLoginMethodSaveRequestDTO request) {
        authAdminService.saveLoginMethodConfig(method, request.getEnabled(),
                request.getUsePlatformConfig(), request.getConfigJson());
        return success();
    }

    // ==================== mapping ====================

    private AuthUserUpdateVO toUpdateVO(AuthUserUpdateRequestDTO request) {
        AuthUserUpdateVO vo = new AuthUserUpdateVO();
        vo.setUsername(request.getUsername());
        vo.setPassword(request.getPassword());
        vo.setEmail(request.getEmail());
        vo.setPhone(request.getPhone());
        vo.setNickname(request.getNickname());
        vo.setAvatar(request.getAvatar());
        vo.setStatus(request.getStatus());
        vo.setRealName(request.getRealName());
        vo.setGender(request.getGender());
        vo.setBirthday(request.getBirthday());
        return vo;
    }

    private AuthUserDTO toUserDTO(AuthUserVO vo) {
        AuthUserDTO dto = new AuthUserDTO();
        dto.setId(vo.getId());
        dto.setUsername(vo.getUsername());
        dto.setEmail(vo.getEmail());
        dto.setPhone(vo.getPhone());
        dto.setNickname(vo.getNickname());
        dto.setAvatar(vo.getAvatar());
        dto.setStatus(vo.getStatus());
        dto.setRoles(vo.getRoles());
        dto.setPermissions(vo.getPermissions());
        dto.setRealName(vo.getRealName());
        dto.setGender(vo.getGender());
        dto.setBirthday(vo.getBirthday());
        dto.setEmailVerified(vo.getEmailVerified());
        dto.setPhoneVerified(vo.getPhoneVerified());
        dto.setCreatedAt(vo.getCreatedAt());
        dto.setLastLoginAt(vo.getLastLoginAt());
        return dto;
    }

    private AuthRoleDTO toRoleDTO(AuthRoleVO vo) {
        AuthRoleDTO dto = new AuthRoleDTO();
        dto.setId(vo.getId());
        dto.setCode(vo.getCode());
        dto.setName(vo.getName());
        dto.setDescription(vo.getDescription());
        dto.setStatus(vo.getStatus());
        dto.setPermissions(vo.getPermissions());
        return dto;
    }

    private AuthPermissionDTO toPermissionDTO(AuthPermissionVO vo) {
        AuthPermissionDTO dto = new AuthPermissionDTO();
        dto.setId(vo.getId());
        dto.setCode(vo.getCode());
        dto.setName(vo.getName());
        dto.setResource(vo.getResource());
        dto.setAction(vo.getAction());
        dto.setDescription(vo.getDescription());
        return dto;
    }

    private AuthLoginMethodDTO toLoginMethodDTO(LoginMethodConfigVO vo) {
        AuthLoginMethodDTO dto = new AuthLoginMethodDTO();
        dto.setMethod(vo.getMethod());
        dto.setCategory(vo.getCategory());
        dto.setDisplayName(vo.getDisplayName());
        dto.setEnabled(vo.getEnabled());
        dto.setUsePlatformConfig(vo.getUsePlatformConfig());
        dto.setHasConfig(vo.getHasConfig());
        dto.setPlatformEnabled(vo.getPlatformEnabled());
        return dto;
    }

    private <T> BaseResponse<T> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    private <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }
}
