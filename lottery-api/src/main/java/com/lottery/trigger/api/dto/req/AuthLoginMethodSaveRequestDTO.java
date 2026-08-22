package com.lottery.trigger.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * 租户级登录方式配置保存：configJson 为明文凭证 JSON，按非空键合并（空串=不修改凭证）
 */
@Data
public class AuthLoginMethodSaveRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "enabled cannot be null")
    private Integer enabled;

    @NotNull(message = "usePlatformConfig cannot be null")
    private Integer usePlatformConfig;

    private String configJson;
}
