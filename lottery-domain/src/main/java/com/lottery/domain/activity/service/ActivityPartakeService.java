package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.UserOrderReqEntity;
import com.lottery.domain.activity.model.entity.UserOrderResEntity;

/**
 * @author 永
 * 参与领域-活动服务接口
 */
public interface ActivityPartakeService {
    /**
     * 创建抽奖单
     */
    UserOrderResEntity createPartakeOrder(UserOrderReqEntity reqEntity);

    /**
     * 创建抽奖单
     */
    UserOrderResEntity createPartakeOrder(String userId, Long activityId);
}
