package com.lottery.trigger.api.dto.req;

import lombok.Data;

/**
 * @author 永
 * 积分支付兑换商品，入参
 */
@Data
public class SkuProductShopCartRequestDTO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * sku 商品
     */
    private Long sku;
}
