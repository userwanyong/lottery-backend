package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AuthOAuthBindingDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String provider;
    private String providerUid;
    private Long createdAt;
}
