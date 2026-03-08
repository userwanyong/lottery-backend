package com.lottery.domain.user.service.impl;

import com.lottery.domain.user.model.vo.UserVO;

/**
 * @author 永
 */
public interface IUserService {
    void sendEmailRegisterCode(String email);

    UserVO registerByEmail(String email, String passCode, String password);

    UserVO loginByEmailPassword(String email, String password);

    UserVO refreshToken(String refreshToken);

    void logout(Long userId);
}
