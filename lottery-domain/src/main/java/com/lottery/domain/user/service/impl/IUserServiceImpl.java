package com.lottery.domain.user.service.impl;

import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IUserServiceImpl implements IUserService {

    @Resource
    private IUserRepository repository;

    @Override
    public void sendEmailRegisterCode(String email) {
        if (StringUtils.isBlank(email)) {
            throw new AppException(ResponseCode.EMAIL_EMPTY.getCode(), ResponseCode.EMAIL_EMPTY.getMessage());
        }
        repository.sendEmailRegisterCode(email.trim().toLowerCase());
    }

    @Override
    public UserVO registerByEmail(String email, String passCode, String password) {
        if (StringUtils.isBlank(email)) {
            throw new AppException(ResponseCode.EMAIL_EMPTY.getCode(), ResponseCode.EMAIL_EMPTY.getMessage());
        }
        if (StringUtils.isBlank(passCode)) {
            throw new AppException(ResponseCode.EMAIL_PASSCODE_EMPTY.getCode(), ResponseCode.EMAIL_PASSCODE_EMPTY.getMessage());
        }
        if (StringUtils.isBlank(password)) {
            throw new AppException(ResponseCode.EMAIL_PASSWORD_EMPTY.getCode(), ResponseCode.EMAIL_PASSWORD_EMPTY.getMessage());
        }
        return repository.registerByEmail(email.trim().toLowerCase(), passCode.trim(), password);
    }

    @Override
    public UserVO loginByEmailPassword(String email, String password) {
        if (StringUtils.isBlank(email)) {
            throw new AppException(ResponseCode.EMAIL_EMPTY.getCode(), ResponseCode.EMAIL_EMPTY.getMessage());
        }
        if (StringUtils.isBlank(password)) {
            throw new AppException(ResponseCode.EMAIL_PASSWORD_EMPTY.getCode(), ResponseCode.EMAIL_PASSWORD_EMPTY.getMessage());
        }
        return repository.loginByEmailPassword(email.trim().toLowerCase(), password);
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new AppException(ResponseCode.REFRESH_TOKEN_INVALID.getCode(), ResponseCode.REFRESH_TOKEN_INVALID.getMessage());
        }
        return repository.refreshToken(refreshToken);
    }

    @Override
    public void logout(Long userId) {
        repository.logout(userId);
    }
}
