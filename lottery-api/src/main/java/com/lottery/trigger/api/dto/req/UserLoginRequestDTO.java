package com.lottery.trigger.api.dto.req;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * @author 永
 */
@Data
public class UserLoginRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
