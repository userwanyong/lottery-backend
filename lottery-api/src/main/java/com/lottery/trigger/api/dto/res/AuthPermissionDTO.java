package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AuthPermissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String resource;
    private String action;
    private String description;
}
