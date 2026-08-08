package com.lottery.trigger.config;

import com.lottery.domain.task.service.TaskService;
import com.lottery.trigger.listener.CreditAdjustSuccessCustomer;
import com.lottery.trigger.listener.RebateMessageCustomer;
import com.lottery.trigger.listener.SendAwardCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 本地消息分发器：按 topic 路由到对应 Customer，执行后更新 task 状态。
 * 即时路径（LocalMessageEventListener，事务后触发）与补偿路径（SendMessageTaskJob，定时扫表）共用本分发器。
 */
@Slf4j
@Component
public class MessageDispatcher {

    @Resource
    private TaskService taskService;
    @Resource
    private SendAwardCustomer sendAwardCustomer;
    @Resource
    private RebateMessageCustomer rebateMessageCustomer;
    @Resource
    private CreditAdjustSuccessCustomer creditAdjustSuccessCustomer;

    /**
     * 分发并按执行结果更新 task 状态（成功 completed / 失败 fail）。
     * 失败时 task 留 fail，由 SendMessageTaskJob 后续重试，保证最终一致性。
     */
    public void dispatch(String topic, String message, String userId, String messageId) {
        try {
            doDispatch(topic, message);
            taskService.updateTaskSendMessageCompleted(userId, messageId);
            log.info("【消息分发】执行成功 userId: {} topic: {}", userId, topic);
        } catch (Exception e) {
            log.error("【消息分发】执行失败 userId: {} topic: {}", userId, topic, e);
            taskService.updateTaskSendMessageFail(userId, messageId);
        }
    }

    private void doDispatch(String topic, String message) {
        if (SendAwardCustomer.TOPIC.equals(topic)) {
            sendAwardCustomer.listener(message);
        } else if (RebateMessageCustomer.TOPIC.equals(topic)) {
            rebateMessageCustomer.listener(message);
        } else if (CreditAdjustSuccessCustomer.TOPIC.equals(topic)) {
            creditAdjustSuccessCustomer.listener(message);
        } else {
            log.warn("【消息分发】未知 topic，无法分发 topic: {}", topic);
        }
    }
}
