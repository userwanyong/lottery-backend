package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.award.event.SendAwardMessageEvent;
import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 发奖（轻量版：由 SendMessageTaskJob 本地分发调用，去 MQ）
 */
@Slf4j
@Component
public class SendAwardCustomer {
    public static final String TOPIC = "send_award";

    @Resource
    private UserAwardService userAwardService;

    public void listener(String message) {
        try {
            log.info("[SendAwardCustomer]监听到用户发奖消息 topic: {} message: {}", TOPIC, message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();
            // 发奖
            DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
            distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
            distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
            distributeAwardEntity.setUserOrderId(sendAwardMessage.getUserOrderId());
            distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
            distributeAwardEntity.setActivityId(sendAwardMessage.getActivityId());
            userAwardService.distributeAward(distributeAwardEntity);
            log.info("[SendAwardCustomer]用户发奖消息，消费成功 topic: {} message: {}", TOPIC, message);
        } catch (Exception e) {
            log.error("[SendAwardCustomer]用户发奖消息，消费失败 topic: {} message: {}", TOPIC, message);
            throw e;
        }
    }
}
