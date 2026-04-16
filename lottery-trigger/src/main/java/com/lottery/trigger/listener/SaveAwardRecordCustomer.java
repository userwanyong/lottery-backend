package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.award.event.SaveAwardRecordMessageEvent;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 保存中奖记录MQ消费者（异步化）
 */
@Slf4j
@Component
public class SaveAwardRecordCustomer {
    @Value("${spring.rabbitmq.topic.save_award_record}")
    private String topic;

    @Resource
    private UserAwardService userAwardService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.save_award_record}"))
    public void listener(String message) {
        try {
            log.info("[SaveAwardRecordCustomer]监听到保存中奖记录消息 topic:{} message:{}", topic, message);
            BaseEvent.EventMessage<SaveAwardRecordMessageEvent.SaveAwardRecordMessage> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SaveAwardRecordMessageEvent.SaveAwardRecordMessage>>() {
                    }.getType());
            SaveAwardRecordMessageEvent.SaveAwardRecordMessage data = eventMessage.getData();

            UserAwardRecordEntity entity = UserAwardRecordEntity.builder()
                    .userId(data.getUserId())
                    .activityId(data.getActivityId())
                    .strategyId(data.getStrategyId())
                    .userOrderId(data.getUserOrderId())
                    .awardId(data.getAwardId())
                    .awardTitle(data.getAwardTitle())
                    .awardConfig(data.getAwardConfig())
                    .awardTime(data.getAwardTime())
                    .awardState(AwardStateVO.create)
                    .build();
            userAwardService.saveUserAwardRecord(entity);
            log.info("[SaveAwardRecordCustomer]保存中奖记录成功 topic:{}", topic);
        } catch (Exception e) {
            log.error("[SaveAwardRecordCustomer]保存中奖记录失败 topic:{} message:{}", topic, message, e);
            throw e;
        }
    }
}
