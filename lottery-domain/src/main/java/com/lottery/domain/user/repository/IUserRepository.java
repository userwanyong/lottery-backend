package com.lottery.domain.user.repository;

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
     * OAuth 回调处理，返回登录用户（含令牌）
     */
    UserVO handleOAuthCallback(String provider, String code, String state);

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
}
