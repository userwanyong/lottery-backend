package com.lottery.domain.auth.model.vo;

import lombok.Data;

/**
 * auth-service 权限
 */
@Data
public class AuthPermissionVO {
    private Long id;
    private String code;
    private String name;
    private String resource;
    private String action;
    private String description;
}
