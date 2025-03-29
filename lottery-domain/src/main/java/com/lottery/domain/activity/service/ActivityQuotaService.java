package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.ActivityAccountEntity;
import com.lottery.domain.activity.model.entity.DeliveryOrderEntity;
import com.lottery.domain.activity.model.entity.QuotaOrderEntity;
import com.lottery.domain.activity.model.entity.UnpaidQuotaOrderEntity;

/**
 * @author 永
 * 活动-额度领域接口
 */
public interface ActivityQuotaService {
    /**
     * 创建 sku 账户充值订单
     */
    UnpaidQuotaOrderEntity createQuotaOrder(QuotaOrderEntity quotaOrderEntity);

    /**
     * 更新 sku 账户充值订单
     */
    void updateQuotaOrder(DeliveryOrderEntity deliveryOrderEntity);

    Integer queryTodayUserLotteryCount(String userId, Long activityId);

    ActivityAccountEntity queryUserActivityAccount(String userId, Long activityId);

    Integer queryTotalUserLotteryCount(String userId, Long activityId);
}
