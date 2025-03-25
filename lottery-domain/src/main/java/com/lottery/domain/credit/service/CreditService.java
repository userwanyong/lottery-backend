package com.lottery.domain.credit.service;

import com.lottery.domain.credit.model.entity.TradeEntity;

/**
 * @author 永
 * 积分领域接口
 */
public interface CreditService {
    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */
    String createCreditOrder(TradeEntity tradeEntity);
}
