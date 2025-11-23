package com.lottery.trigger.api.dto.req;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 永
 */
@Data
public class UserLoginRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
