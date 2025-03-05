package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import com.lottery.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @author 永
 * 抽奖活动订单接口
 */
public interface Order {
    /**
     * 以sku创建抽奖活动订单，获得次数
     *
     * @param activityShopCartEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    ActivityOrderEntity createActivityOrder(ActivityShopCartEntity activityShopCartEntity);

}
