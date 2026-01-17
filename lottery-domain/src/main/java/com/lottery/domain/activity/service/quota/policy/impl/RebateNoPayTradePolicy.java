package com.lottery.domain.activity.service.quota.policy.impl;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.valobj.OrderStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.quota.policy.TradePolicy;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 永
 * 返利类型订单
 */
@Service(Constants.QuotaModel.REBATE_NO_PAY_TRADE)
public class RebateNoPayTradePolicy implements TradePolicy {

    @Resource
    private ActivityRepository repository;
    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.getActivityOrderEntity().setState(OrderStateVO.complete);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        repository.doSaveNoPayOrder(createQuotaOrderAggregate);
    }
}
