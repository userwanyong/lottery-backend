package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class AuthUserStatusRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "status cannot be null")
    private Integer status;
}
