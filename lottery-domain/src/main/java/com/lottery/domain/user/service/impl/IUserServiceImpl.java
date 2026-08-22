package com.lottery.domain.user.service.impl;

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
    public UserVO handleOAuthCallback(String provider, String code, String state) {
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
}
