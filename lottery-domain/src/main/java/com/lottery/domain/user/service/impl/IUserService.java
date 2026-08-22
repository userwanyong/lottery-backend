package com.lottery.domain.user.service.impl;

import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.OAuthBindingVO;
import com.lottery.domain.user.model.vo.OAuthCallbackResultVO;
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

    OAuthCallbackResultVO handleOAuthCallback(String provider, String code, String state);

    UserVO refreshToken(String refreshToken);

    UserVO getUserById(Long userId);

    void logout(String accessToken, String refreshToken);

    // ==================== 个人中心 ====================

    AuthUserVO getProfile(Long userId);

    void updateProfile(Long userId, AuthUserUpdateVO updateVO);

    void changePassword(Long userId, String oldPassword, String newPassword);

    String uploadAvatar(Long userId, String filename, String contentType, byte[] data);

    List<OAuthBindingVO> listOAuthBindings(Long userId);

    String buildBindAuthorizeUrl(Long userId, String provider);

    void unbindOAuth(Long userId, String provider);

    void bindEmail(Long userId, String method, String target, String code);

    void unbindEmail(Long userId);

    void bindPhone(Long userId, String method, String target, String code);

    void unbindPhone(Long userId);
}
