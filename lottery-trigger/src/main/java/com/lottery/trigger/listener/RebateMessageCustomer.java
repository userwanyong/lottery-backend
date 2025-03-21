package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.activity.model.entity.QuotaOrderEntity;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.rebate.event.SendRebateMessageEvent;
import com.lottery.domain.rebate.model.valobj.RebateTypeVO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.event.BaseEvent;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @RabbitListener(queuesToDeclare= @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户入账消息 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.RebateMessage data = eventMessage.getData();
            if (!RebateTypeVO.SKU.getCode().equals(data.getRebateType())){
                log.info("监听用户入账消息，非suk奖励，暂时不处理 topic: {} message: {}", topic, message);
                return;
            }
            QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
            quotaOrderEntity.setSku(Long.valueOf(data.getRebateConfig()));
            quotaOrderEntity.setUserId(data.getUserId());
            quotaOrderEntity.setOutBusinessNo(data.getBizId());
            String quotaOrder = activityQuotaService.createQuotaOrder(quotaOrderEntity);
            log.info("监听用户入账消息，入账成功 topic: {} message: {} quotaOrder: {}", topic, message,quotaOrder);
        }catch (AppException ae){
            if (ResponseCode.INDEX_DUP.getCode()==ae.getCode()) {
                log.error("监听用户入账消息，重复消费 topic: {} message: {}", topic, message);
                return;
            }
            throw ae;
        }
        catch (Exception e){
            log.error("监听用户入账消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
