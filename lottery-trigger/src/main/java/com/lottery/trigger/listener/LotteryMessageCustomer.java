package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.channel.service.ChannelService;
import com.lottery.domain.strategy.event.SendLotteryMessageEvent;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.event.BaseEvent;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 永
 * 广播中奖消息
 */
@Slf4j
@Component
public class LotteryMessageCustomer {
    @Value("${spring.rabbitmq.topic.lottery}")
    private String topic;

    @Resource
    private ChannelService channelService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.lottery}"))
    public void listener(String message) {
        try {
            log.info("[RebateMessageCustomer]监听到中奖广播消息 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendLotteryMessageEvent.LotteryMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendLotteryMessageEvent.LotteryMessage>>() {
            }.getType());
            SendLotteryMessageEvent.LotteryMessage data = eventMessage.getData();
            if (data.getAwardId()==0L){
                //说明是兜底奖，不进行广播
                return;
            }
            String activityId = data.getActivityId();
            Date awardTime = data.getAwardTime();
            //格式化为yyyy/mm/dd的形式
            String formatTime = new SimpleDateFormat("yyyy/MM/dd").format(awardTime);
            // 用户脱敏 取用户id的前两位和后两位 因为用的是19位的雪花id，不需要考虑少于4位的情况
            String userId = data.getUserId().substring(0, 2) + "***" + data.getUserId().substring(data.getUserId().length() - 2);
            String title=data.getAwardTitle();
            if (!(data.getAwardId()==1947135858008137730L||data.getAwardId()==101)){
                // 暂时通过id判断是否是随机积分
                title="<div style=\"color: red;\">"+data.getAwardTitle()+"</div>";
            }
            String ms = "恭喜用户<div style=\"color: #1890ff;\">" + userId + "</div>抽中&nbsp;" +title +"&nbsp;<div style=\"font-size: 10px;\">" +formatTime+"</div>";
            channelService.sendToAllClient(activityId,ms);
        } catch (AppException ae) {
            if (ResponseCode.INDEX_DUP.getCode() == ae.getCode()) {
                log.warn("[RebateMessageCustomer]中奖广播消息，重复消费 topic: {} message: {}", topic, message);
                return;
            }
            log.error("[RebateMessageCustomer]中奖广播失败 topic: {} message: {} code:{} info:{}", topic, message, ae.getCode(), ae.getMessage());
            throw ae;
        } catch (Exception e) {
            log.error("[RebateMessageCustomer]中奖广播失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
