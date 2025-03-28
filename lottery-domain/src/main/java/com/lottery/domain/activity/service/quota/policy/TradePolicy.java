package com.lottery.domain.activity.service.quota.policy;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author 永
 * 订单类型接口
 */
public interface TradePolicy {
    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
