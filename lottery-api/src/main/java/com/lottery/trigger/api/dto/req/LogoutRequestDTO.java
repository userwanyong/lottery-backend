package com.lottery.trigger.api.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登出请求：refreshToken 可选，传入则一并撤销
 */
@Data
public class LogoutRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String refreshToken;
}
