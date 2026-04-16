package com.lottery.domain.award.event;

import com.lottery.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 永
 * 保存中奖记录消息事件（异步化）
 */
@Component
public class SaveAwardRecordMessageEvent extends BaseEvent<SaveAwardRecordMessageEvent.SaveAwardRecordMessage> {
    @Value("${spring.rabbitmq.topic.save_award_record}")
    private String topic;

    @Override
    public EventMessage<SaveAwardRecordMessage> buildEventMessage(SaveAwardRecordMessage data) {
        return EventMessage.<SaveAwardRecordMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaveAwardRecordMessage {
        private String userId;
        private Long activityId;
        private Long strategyId;
        private Long userOrderId;
        private Long awardId;
        private String awardTitle;
        private String awardConfig;
        private Date awardTime;
    }
}
