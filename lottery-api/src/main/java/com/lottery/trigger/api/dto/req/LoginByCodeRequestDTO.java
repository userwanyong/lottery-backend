package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码登录：未知邮箱/手机号由 auth-service 自动注册
 */
@Data
public class LoginByCodeRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "method cannot be blank")
    private String method;

    @NotBlank(message = "target cannot be blank")
    private String target;

    @NotBlank(message = "code cannot be blank")
    private String code;
}
