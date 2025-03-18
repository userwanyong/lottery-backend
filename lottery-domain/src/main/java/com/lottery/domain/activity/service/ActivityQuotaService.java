package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.QuotaOrderEntity;

/**
 * @author 永
 * 活动-额度领域接口
 */
public interface ActivityQuotaService {
    /**
     * 创建 sku 账户充值订单，给用户增加抽奖次数
     *
     * @param quotaOrderEntity 活动商品充值实体对象
     * @return 活动ID
     */
    String createQuotaOrder(QuotaOrderEntity quotaOrderEntity);

    Integer queryTodayUserLotteryCount(String userId, Long activityId);
}
