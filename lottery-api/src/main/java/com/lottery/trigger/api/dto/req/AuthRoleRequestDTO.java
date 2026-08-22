package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
public class AuthRoleRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 仅创建时必填，更新忽略
     */
    private String code;

    @NotBlank(message = "name cannot be blank")
    private String name;

    private String description;
}
