package com.lottery.domain.user.repository;

import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.model.vo.WechatMiniProgramQrCodeVO;

/**
 * @author 永
 */
public interface IUserRepository {
    void sendEmailRegisterCode(String email);

    UserVO registerByEmail(String email, String passCode, String password);

    UserVO loginByEmailPassword(String email, String password);

    UserVO refreshToken(String refreshToken);

    WechatMiniProgramQrCodeVO generateWechatMiniProgramLoginQrCode();

    WechatMiniProgramQrCodeVO queryWechatMiniProgramLoginQrCodeStatus(String qrcodeId);

    UserVO loginByWechatMiniProgramQrCode(String ticket);

    void logout(Long userId);
}
