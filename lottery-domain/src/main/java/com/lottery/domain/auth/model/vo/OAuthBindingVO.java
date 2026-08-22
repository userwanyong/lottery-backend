package com.lottery.domain.auth.model.vo;

import lombok.Data;

/**
 * 第三方账号绑定
 */
@Data
public class OAuthBindingVO {
    private Long id;
    /**
     * gitee / github
     */
    private String provider;
    private String providerUid;
    /**
     * 绑定时间 epoch 毫秒
     */
    private Long createdAt;
}
