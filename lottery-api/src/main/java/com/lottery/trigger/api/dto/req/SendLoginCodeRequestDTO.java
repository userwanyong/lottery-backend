package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * 发送登录验证码：method 为 auth-service 登录方式编码（email:aliyun / email:smtp / sms:aliyun），
 * target 为邮箱地址或手机号
 */
@Data
public class SendLoginCodeRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "method cannot be blank")
    private String method;

    @NotBlank(message = "target cannot be blank")
    private String target;
}
