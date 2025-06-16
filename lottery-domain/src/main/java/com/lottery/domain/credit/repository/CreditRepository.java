package com.lottery.domain.credit.repository;

import com.lottery.domain.credit.model.aggregate.TradeAggregate;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;

/**
 * @author 永
 * 积分领域仓储接口
 */
public interface CreditRepository {
    void saveTradeAggregate(TradeAggregate tradeAggregate);

    CreditAccountEntity queryUserCreditAccount(String userId,Long activityId);
}
