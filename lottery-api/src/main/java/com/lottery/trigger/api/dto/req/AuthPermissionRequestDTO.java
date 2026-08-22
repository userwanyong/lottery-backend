package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
public class AuthPermissionRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "code cannot be blank")
    private String code;

    @NotBlank(message = "name cannot be blank")
    private String name;

    private String resource;
    private String action;
    private String description;
}
