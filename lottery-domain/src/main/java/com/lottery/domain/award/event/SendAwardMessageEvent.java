package com.lottery.domain.award.event;

import com.lottery.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 永
 * 奖品事件消息（轻量版：topic 硬编码，去 rabbitmq 配置）
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessageEvent.SendAwardMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return "send_award";
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendAwardMessage {
        /** 用户ID */
        private String userId;
        /** 活动ID */
        private Long activityId;
        /** 奖品ID */
        private Long awardId;
        /** 奖品标题（名称） */
        private String awardTitle;
        /** 订单ID */
        private Long userOrderId;
        /** 奖品配置信息 */
        private String awardConfig;

    }
}
