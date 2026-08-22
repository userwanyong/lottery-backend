package com.lottery.domain.auth.model.vo;

import lombok.Data;

import java.util.List;

/**
 * auth-service 用户信息（管理端与个人中心共用）
 */
@Data
public class AuthUserVO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    /**
     * 1-正常 0-禁用
     */
    private Integer status;
    private List<String> roles;
    private List<String> permissions;
    private String realName;
    /**
     * 0-未知 1-男 2-女
     */
    private Integer gender;
    /**
     * ISO 日期 yyyy-MM-dd
     */
    private String birthday;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    /**
     * epoch 毫秒，0=未知
     */
    private Long createdAt;
    private Long lastLoginAt;
}
