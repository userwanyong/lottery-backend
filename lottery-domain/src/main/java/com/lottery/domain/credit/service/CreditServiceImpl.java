package com.lottery.domain.credit.service;

import com.lottery.domain.credit.model.aggregate.TradeAggregate;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.CreditOrderEntity;
import com.lottery.domain.credit.model.entity.TradeEntity;
import com.lottery.domain.credit.repository.CreditRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 永
 * 积分领域实现类
 */
@Service
public class CreditServiceImpl implements CreditService{
    @Resource
    private CreditRepository repository;
    @Override
    public String createCreditOrder(TradeEntity tradeEntity) {
        // 构建积分账户实体
        CreditAccountEntity creditAccountEntity = TradeAggregate.buildCreditAccountEntity(tradeEntity.getUserId(), tradeEntity.getAmount());
        // 构建积分订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.buildCreditOrderEntity(tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getTradeType(), tradeEntity.getAmount(), tradeEntity.getOutBusinessNo());
        // 构建聚合对象
        TradeAggregate tradeAggregate = new TradeAggregate();
        tradeAggregate.setUserId(tradeEntity.getUserId());
        tradeAggregate.setCreditAccountEntity(creditAccountEntity);
        tradeAggregate.setCreditOrderEntity(creditOrderEntity);
        // 保存
        repository.saveTradeAggregate(tradeAggregate);
        // 返回单号
        return creditOrderEntity.getOrderId();
    }
}
