package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 认证服务用户信息（管理端与个人中心共用）
 */
@Data
public class AuthUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private List<String> roles;
    private List<String> permissions;
    private String realName;
    private Integer gender;
    private String birthday;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private Long createdAt;
    private Long lastLoginAt;
}
