package com.lottery.domain.auth.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户资料更新参数：null 表示不修改；字符串空串表示清空（email/phone/nickname/avatar）
 */
@Data
public class AuthUserUpdateVO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private String realName;
    private Integer gender;
    private String birthday;

    /**
     * 依据非空字段生成 auth-service updateUser 的字段掩码
     */
    public List<String> buildFieldsToUpdate() {
        List<String> fields = new ArrayList<>();
        if (username != null) {
            fields.add("username");
        }
        if (password != null) {
            fields.add("password");
        }
        if (email != null) {
            fields.add("email");
        }
        if (phone != null) {
            fields.add("phone");
        }
        if (nickname != null) {
            fields.add("nickname");
        }
        if (avatar != null) {
            fields.add("avatar");
        }
        if (status != null) {
            fields.add("status");
        }
        if (realName != null) {
            fields.add("realName");
        }
        if (gender != null) {
            fields.add("gender");
        }
        if (birthday != null) {
            fields.add("birthday");
        }
        return fields;
    }
}
