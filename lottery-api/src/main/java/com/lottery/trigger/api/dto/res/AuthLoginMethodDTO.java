package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录方式配置（凭证脱敏，仅配置状态）
 */
@Data
public class AuthLoginMethodDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String method;
    private String category;
    private String displayName;
    private Integer enabled;
    private Integer usePlatformConfig;
    private Boolean hasConfig;
    private Boolean platformEnabled;
}
