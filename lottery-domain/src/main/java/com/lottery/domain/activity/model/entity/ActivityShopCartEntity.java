package com.lottery.domain.activity.model.entity;

import lombok.Data;

/**
 * @author 永
 * 活动购物车实体对象
 */
@Data
public class ActivityShopCartEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;

}
