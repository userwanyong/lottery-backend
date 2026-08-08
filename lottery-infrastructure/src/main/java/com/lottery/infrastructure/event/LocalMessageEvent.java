package com.lottery.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 本地消息事件（轻量版：替代 RabbitMQ）。
 * 业务事务提交后由 LocalMessageEventListener 异步分发到对应 Customer，
 * 失败/崩溃的由 SendMessageTaskJob 扫 task 表补偿，语义等同原 MQ + 本地消息表。
 */
@Getter
public class LocalMessageEvent extends ApplicationEvent {
    private final String topic;
    private final String message;
    private final String userId;
    private final String messageId;

    public LocalMessageEvent(Object source, String topic, String message, String userId, String messageId) {
        super(source);
        this.topic = topic;
        this.message = message;
        this.userId = userId;
        this.messageId = messageId;
    }
}
