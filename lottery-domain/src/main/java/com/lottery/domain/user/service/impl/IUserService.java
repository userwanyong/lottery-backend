package com.lottery.domain.user.service.impl;

import com.lottery.domain.user.model.vo.UserVO;

import java.util.List;

/**
 * @author 永
 */
public interface IUserService {

    List<String> listEnabledLoginMethods();

    UserVO loginByPassword(String username, String password);

    void sendLoginCode(String method, String target);

    UserVO loginByCode(String method, String target, String code);

    String buildOAuthAuthorizeUrl(String provider);

    UserVO handleOAuthCallback(String provider, String code, String state);

    UserVO refreshToken(String refreshToken);

    UserVO getUserById(Long userId);

    void logout(String accessToken, String refreshToken);
}
