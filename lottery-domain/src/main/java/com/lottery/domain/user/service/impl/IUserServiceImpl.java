package com.lottery.domain.user.service.impl;

import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.OAuthBindingVO;
import com.lottery.domain.user.model.vo.OAuthCallbackResultVO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IUserServiceImpl implements IUserService {

    private static final long MAX_AVATAR_BYTES = 2 * 1024 * 1024;

    @Resource
    private IUserRepository repository;

    @Override
    public List<String> listEnabledLoginMethods() {
        return repository.listEnabledLoginMethods();
    }

    @Override
    public UserVO loginByPassword(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "用户名和密码不能为空");
        }
        return repository.loginByPassword(username.trim(), password);
    }

    @Override
    public void sendLoginCode(String method, String target) {
        if (StringUtils.isBlank(method) || StringUtils.isBlank(target)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "登录方式与接收目标不能为空");
        }
        repository.sendLoginCode(method.trim(), target.trim());
    }

    @Override
    public UserVO loginByCode(String method, String target, String code) {
        if (StringUtils.isBlank(method) || StringUtils.isBlank(target) || StringUtils.isBlank(code)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "登录方式、接收目标与验证码不能为空");
        }
        return repository.loginByCode(method.trim(), target.trim(), code.trim());
    }

    @Override
    public String buildOAuthAuthorizeUrl(String provider) {
        if (StringUtils.isBlank(provider)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "provider cannot be blank");
        }
        return repository.buildOAuthAuthorizeUrl(provider.trim().toLowerCase());
    }

    @Override
    public OAuthCallbackResultVO handleOAuthCallback(String provider, String code, String state) {
        if (StringUtils.isBlank(provider) || StringUtils.isBlank(code) || StringUtils.isBlank(state)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "provider、code 与 state 不能为空");
        }
        return repository.handleOAuthCallback(provider.trim().toLowerCase(), code.trim(), state.trim());
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
        }
        return repository.refreshToken(refreshToken.trim());
    }

    @Override
    public UserVO getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "userId cannot be blank");
        }
        return repository.getUserById(userId);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        repository.logout(accessToken, refreshToken);
    }

    // ==================== 个人中心 ====================

    @Override
    public AuthUserVO getProfile(Long userId) {
        return repository.getProfile(userId);
    }

    @Override
    public void updateProfile(Long userId, AuthUserUpdateVO updateVO) {
        if (updateVO == null || updateVO.buildFieldsToUpdate().isEmpty()) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "未指定任何需要更新的字段");
        }
        if (updateVO.getPassword() != null || updateVO.getUsername() != null || updateVO.getStatus() != null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "个人中心不允许修改用户名/密码/状态");
        }
        repository.updateProfile(userId, updateVO);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "旧密码与新密码不能为空");
        }
        if (newPassword.length() < 6 || newPassword.length() > 50) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "新密码长度须为 6~50 位");
        }
        repository.changePassword(userId, oldPassword, newPassword);
    }

    @Override
    public String uploadAvatar(Long userId, String filename, String contentType, byte[] data) {
        if (data == null || data.length == 0) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "头像文件不能为空");
        }
        if (data.length > MAX_AVATAR_BYTES) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "头像文件不能超过 2MB");
        }
        return repository.uploadAvatar(userId, filename, contentType, data);
    }

    @Override
    public List<OAuthBindingVO> listOAuthBindings(Long userId) {
        return repository.listOAuthBindings(userId);
    }

    @Override
    public String buildBindAuthorizeUrl(Long userId, String provider) {
        if (StringUtils.isBlank(provider)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "provider cannot be blank");
        }
        return repository.buildBindAuthorizeUrl(userId, provider.trim().toLowerCase());
    }

    @Override
    public void unbindOAuth(Long userId, String provider) {
        if (StringUtils.isBlank(provider)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "provider cannot be blank");
        }
        repository.unbindOAuth(userId, provider.trim().toLowerCase());
    }

    @Override
    public void bindEmail(Long userId, String method, String target, String code) {
        if (StringUtils.isBlank(method) || StringUtils.isBlank(target) || StringUtils.isBlank(code)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "登录方式、邮箱与验证码不能为空");
        }
        repository.bindEmail(userId, method.trim(), target.trim(), code.trim());
    }

    @Override
    public void unbindEmail(Long userId) {
        repository.unbindEmail(userId);
    }

    @Override
    public void bindPhone(Long userId, String method, String target, String code) {
        if (StringUtils.isBlank(method) || StringUtils.isBlank(target) || StringUtils.isBlank(code)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "登录方式、手机号与验证码不能为空");
        }
        repository.bindPhone(userId, method.trim(), target.trim(), code.trim());
    }

    @Override
    public void unbindPhone(Long userId) {
        repository.unbindPhone(userId);
    }
}
