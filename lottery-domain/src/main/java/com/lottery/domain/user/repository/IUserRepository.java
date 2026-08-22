package com.lottery.domain.user.repository;

import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.OAuthBindingVO;
import com.lottery.domain.user.model.vo.OAuthCallbackResultVO;
import com.lottery.domain.user.model.vo.UserVO;

import java.util.List;

/**
 * @author 永
 * 用户认证仓储端口，实现由 auth-service（Dubbo Triple RPC）承担
 */
public interface IUserRepository {

    /**
     * 当前租户启用的登录方式（与 auth-service 管理端开闭同步）
     */
    List<String> listEnabledLoginMethods();

    /**
     * 账号密码登录（用户名或邮箱）
     */
    UserVO loginByPassword(String username, String password);

    /**
     * 发送邮箱/短信登录验证码
     */
    void sendLoginCode(String method, String target);

    /**
     * 验证码登录（未知用户自动注册）
     */
    UserVO loginByCode(String method, String target, String code);

    /**
     * 生成 OAuth 授权页跳转地址
     */
    String buildOAuthAuthorizeUrl(String provider);

    /**
     * OAuth 回调处理：登录流返回用户（含令牌）；绑定流返回绑定结果
     */
    OAuthCallbackResultVO handleOAuthCallback(String provider, String code, String state);

    /**
     * 刷新令牌对（轮换，旧的立即失效）
     */
    UserVO refreshToken(String refreshToken);

    /**
     * 查询用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 登出：拉黑 accessToken 并删除 refreshToken
     */
    void logout(String accessToken, String refreshToken);

    // ==================== 个人中心 ====================

    /**
     * 个人完整资料（含角色/权限/联系方式验证状态）
     */
    AuthUserVO getProfile(Long userId);

    /**
     * 更新自己的资料（昵称/头像/邮箱/手机/真实姓名/性别/生日）
     */
    void updateProfile(Long userId, AuthUserUpdateVO updateVO);

    /**
     * 修改自己的密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 上传头像，返回可访问 URL
     */
    String uploadAvatar(Long userId, String filename, String contentType, byte[] data);

    /**
     * 已绑定的第三方账号
     */
    List<OAuthBindingVO> listOAuthBindings(Long userId);

    /**
     * 发起第三方账号绑定授权，返回授权页 URL
     */
    String buildBindAuthorizeUrl(Long userId, String provider);

    /**
     * 解绑第三方账号
     */
    void unbindOAuth(Long userId, String provider);

    /**
     * 验证码绑定新邮箱
     */
    void bindEmail(Long userId, String method, String target, String code);

    /**
     * 解绑邮箱
     */
    void unbindEmail(Long userId);

    /**
     * 验证码绑定新手机号
     */
    void bindPhone(Long userId, String method, String target, String code);

    /**
     * 解绑手机号
     */
    void unbindPhone(Long userId);
}
