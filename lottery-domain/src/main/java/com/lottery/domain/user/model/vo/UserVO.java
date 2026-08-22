package com.lottery.domain.user.model.vo;


import lombok.Data;

import java.util.List;

/**
 * @author 永
 */
@Data
public class UserVO {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色编码，如 ROLE_ADMIN
     */
    private List<String> roles;

    /**
     * 权限编码
     */
    private List<String> permissions;

    private String accessToken;

    private String refreshToken;

    /**
     * Access Token 有效期（秒）
     */
    private Long expiresIn;
}
