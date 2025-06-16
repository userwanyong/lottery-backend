package com.lottery.domain.credit.service;

import com.lottery.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.lottery.domain.credit.model.aggregate.TradeAggregate;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.CreditOrderEntity;
import com.lottery.domain.credit.model.entity.TaskEntity;
import com.lottery.domain.credit.model.entity.TradeEntity;
import com.lottery.domain.credit.repository.CreditRepository;
import com.lottery.types.event.BaseEvent;
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
    @Resource
    private CreditAdjustSuccessMessageEvent creditAdjustSuccessMessageEvent;
    @Override
    public String createCreditOrder(TradeEntity tradeEntity) {
        // 构建积分账户实体
        CreditAccountEntity creditAccountEntity = TradeAggregate.buildCreditAccountEntity(tradeEntity.getUserId(),tradeEntity.getActivityId(), tradeEntity.getAmount());
        // 构建积分订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.buildCreditOrderEntity(tradeEntity.getUserId(),tradeEntity.getActivityId(), tradeEntity.getTradeName(), tradeEntity.getTradeType(), tradeEntity.getAmount(), tradeEntity.getOutBusinessNo());
        // 构建消息对象
        CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = new CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage();
        creditAdjustSuccessMessage.setUserId(tradeEntity.getUserId());
        creditAdjustSuccessMessage.setActivityId(tradeEntity.getActivityId());
        creditAdjustSuccessMessage.setOrderId(creditOrderEntity.getOrderId());
        creditAdjustSuccessMessage.setAmount(creditOrderEntity.getTradeAmount());
        creditAdjustSuccessMessage.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());
        BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> creditAdjustSuccessMessageEventMessage = creditAdjustSuccessMessageEvent.buildEventMessage(creditAdjustSuccessMessage);
        // 构建任务对象
        TaskEntity taskEntity = TradeAggregate.buildTaskEntity(tradeEntity.getUserId(),tradeEntity.getActivityId(), creditAdjustSuccessMessageEvent.topic(), creditAdjustSuccessMessageEventMessage.getId(), creditAdjustSuccessMessageEventMessage);
        // 构建聚合对象
        TradeAggregate tradeAggregate = new TradeAggregate();
        tradeAggregate.setUserId(tradeEntity.getUserId());
        tradeAggregate.setCreditAccountEntity(creditAccountEntity);
        tradeAggregate.setCreditOrderEntity(creditOrderEntity);
        tradeAggregate.setTaskEntity(taskEntity);
        // 保存
        repository.saveTradeAggregate(tradeAggregate);
        // 返回单号
        return creditOrderEntity.getOrderId();
    }

    @Override
    public CreditAccountEntity queryUserCreditAccount(String userId,Long activityId) {
        return repository.queryUserCreditAccount(userId,activityId);
    }
}
