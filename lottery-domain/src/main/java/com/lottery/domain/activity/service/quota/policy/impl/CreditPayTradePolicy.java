package com.lottery.domain.activity.service.quota.policy.impl;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.valobj.OrderStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.quota.policy.TradePolicy;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 永
 * 积分类型订单
 */
@Service(Constants.QuotaModel.CREDIT_PAY_TRADE)
public class CreditPayTradePolicy implements TradePolicy {

    @Resource
    private ActivityRepository repository;
    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.getActivityOrderEntity().setState(OrderStateVO.wait_pay);
        repository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
