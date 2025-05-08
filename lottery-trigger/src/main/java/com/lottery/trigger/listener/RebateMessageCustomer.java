package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.activity.model.entity.QuotaOrderEntity;
import com.lottery.domain.activity.model.valobj.OrderTradeTypeVO;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.credit.model.entity.TradeEntity;
import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import com.lottery.domain.credit.service.CreditService;
import com.lottery.domain.rebate.event.SendRebateMessageEvent;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.event.BaseEvent;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 永
 * 入账
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Resource
    private ActivityQuotaService activityQuotaService;
    @Resource
    private CreditService creditService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("[RebateMessageCustomer]监听到用户入账消息 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.RebateMessage data = eventMessage.getData();
            switch (data.getRebateType()) {
                case "sku":
                    QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
                    quotaOrderEntity.setSku(Long.valueOf(data.getRebateConfig()));
                    quotaOrderEntity.setUserId(data.getUserId());
                    quotaOrderEntity.setOutBusinessNo(data.getBizId());
                    quotaOrderEntity.setOrderTradeTypeVO(OrderTradeTypeVO.rebate_no_pay_trade);
                    activityQuotaService.createQuotaOrder(quotaOrderEntity);
                    log.info("[RebateMessageCustomer]用户入账消息，抽奖额度入账成功 topic: {} message: {} ", topic, message);
                    break;
                case "integral":
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setUserId(data.getUserId());
                    tradeEntity.setOutBusinessNo(data.getBizId());
                    tradeEntity.setTradeName(TradeNameVO.REBATE);
                    tradeEntity.setTradeType(TradeTypeVO.FORWARD);
                    tradeEntity.setAmount(new BigDecimal(data.getRebateConfig()));
                    String creditOrder = creditService.createCreditOrder(tradeEntity);
                    log.info("[RebateMessageCustomer]用户入账消息，积分入账成功 topic: {} message: {} creditOrder: {}", topic, message, creditOrder);
                    break;
            }
        } catch (AppException ae) {
            if (ResponseCode.INDEX_DUP.getCode() == ae.getCode()) {
                log.error("[RebateMessageCustomer]用户入账消息，重复消费 topic: {} message: {}", topic, message);
                return;
            }
            log.error("[RebateMessageCustomer]用户入账消息，消费失败 topic: {} message: {} code:{} info:{}", topic, message,ae.getCode(),ae.getMessage());
            throw ae;
        } catch (Exception e) {
            log.error("[RebateMessageCustomer]用户入账消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
