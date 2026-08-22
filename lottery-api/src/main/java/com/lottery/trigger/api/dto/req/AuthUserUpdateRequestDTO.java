package com.lottery.trigger.api.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户资料更新：所有字段可选，null=不修改；email/phone/nickname/avatar 传空串=清空。
 * 个人中心场景由服务层限制仅允许资料字段。
 */
@Data
public class AuthUserUpdateRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    /**
     * 1-正常 0-禁用（仅管理端）
     */
    private Integer status;
    private String realName;
    /**
     * 0-未知 1-男 2-女
     */
    private Integer gender;
    /**
     * ISO 日期 yyyy-MM-dd
     */
    private String birthday;
}
