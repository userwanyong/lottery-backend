package com.lottery.infrastructure.event;

import com.alibaba.fastjson.JSON;
import com.lottery.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * @description 消息发送
 */
@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic, messageJson);
            log.info("[EventPublisher]发送MQ消息成功 topic:{} message:{}", topic, messageJson);
        } catch (Exception e) {
            log.error("[EventPublisher]发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }
    }

    public void publish(String topic, String eventMessage) {
        try {
            rabbitTemplate.convertAndSend(topic, eventMessage);
            log.info("[EventPublisher]发送MQ消息成功 topic:{} message:{}", topic, eventMessage);
        } catch (Exception e) {
            log.error("[EventPublisher]发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }
    }

}

