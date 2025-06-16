package com.lottery.domain.credit.service;

import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.TradeEntity;

/**
 * @author 永
 * 积分领域接口
 */
public interface CreditService {
    /**
     * 创建积分额度单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */
    String createCreditOrder(TradeEntity tradeEntity);

    /**
     * 查询用户积分
     * @param userId 用户id
     * @return CreditAccountEntity
     */
    CreditAccountEntity queryUserCreditAccount(String userId,Long activityId);
}
