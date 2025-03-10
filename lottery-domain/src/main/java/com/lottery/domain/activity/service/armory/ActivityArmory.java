package com.lottery.domain.activity.service.armory;

/**
 * @author 永
 * 活动装配接口 -预热
 */
public interface ActivityArmory {

    /**
     * 活动sku装配
     * @param sku sku
     * @return t/f
     */
    boolean assembleActivitySku(Long sku);

    boolean assembleActivitySkuByActivityId(Long activityId);
}
