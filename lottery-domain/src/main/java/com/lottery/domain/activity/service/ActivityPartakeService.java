package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.PartakeOrderReqEntity;
import com.lottery.domain.activity.model.entity.PartakeOrderResEntity;

/**
 * @author 永
 * 活动-参与领域接口
 */
public interface ActivityPartakeService {
    /**
     * 创建抽奖单
     */
    PartakeOrderResEntity createPartakeOrder(PartakeOrderReqEntity reqEntity);

    /**
     * 创建抽奖单
     */
    PartakeOrderResEntity createPartakeOrder(String userId, Long activityId);
}
