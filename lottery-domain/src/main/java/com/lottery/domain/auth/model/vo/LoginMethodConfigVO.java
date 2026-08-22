package com.lottery.domain.auth.model.vo;

import lombok.Data;

/**
 * 登录方式配置（租户级视角，凭证脱敏仅返回配置状态）
 */
@Data
public class LoginMethodConfigVO {
    /**
     * 登录方式编码，如 oauth:gitee
     */
    private String method;
    /**
     * password / email / sms / oauth
     */
    private String category;
    private String displayName;
    /**
     * 本租户是否启用：0-否 1-是
     */
    private Integer enabled;
    /**
     * 1=使用平台凭证 0=使用自有凭证
     */
    private Integer usePlatformConfig;
    /**
     * 生效凭证是否已配置
     */
    private Boolean hasConfig;
    /**
     * 平台是否已开启该方式
     */
    private Boolean platformEnabled;
}
