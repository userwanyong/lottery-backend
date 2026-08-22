package com.lottery.domain.user.model.vo;

import lombok.Data;

/**
 * OAuth 回调处理结果：login=true 为登录流（携带用户与令牌）；
 * login=false 为绑定流（个人中心发起），看 bindSuccess 与 message。
 */
@Data
public class OAuthCallbackResultVO {
    /**
     * 是否登录流程
     */
    private boolean login;
    /**
     * 绑定流程是否成功
     */
    private boolean bindSuccess;
    private String message;
    /**
     * 登录流程：用户与令牌
     */
    private UserVO user;
}
