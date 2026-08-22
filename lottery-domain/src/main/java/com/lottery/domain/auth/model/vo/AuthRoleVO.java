package com.lottery.domain.auth.model.vo;

import lombok.Data;

import java.util.List;

/**
 * auth-service 角色
 */
@Data
public class AuthRoleVO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer status;
    private List<String> permissions;
}
