package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码绑定邮箱/手机：method 须与发码时一致（email:aliyun/email:smtp/sms:aliyun）
 */
@Data
public class ProfileBindContactRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "method cannot be blank")
    private String method;

    @NotBlank(message = "target cannot be blank")
    private String target;

    @NotBlank(message = "code cannot be blank")
    private String code;
}
