package com.lottery.trigger.listener;

import com.lottery.infrastructure.event.LocalMessageEvent;
import com.lottery.trigger.config.MessageDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 本地消息事件监听器（轻量版：替代 RabbitMQ 消费者）。
 * 业务事务提交后（AFTER_COMMIT）触发，将消息投递到线程池异步分发，
 * 达到与原 MQ 一致的"事务后准实时执行"效果。
 * 若应用在投递后、执行前崩溃，task 仍为 create，由 SendMessageTaskJob 补偿。
 */
@Slf4j
@Component
public class LocalMessageEventListener {

    @Resource
    private MessageDispatcher messageDispatcher;
    @Resource
    private ThreadPoolExecutor executor;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(LocalMessageEvent event) {
        // 提交到线程池异步执行，不阻塞事务提交线程（fallbackExecution=true：无事务上下文时立即触发）
        executor.execute(() -> messageDispatcher.dispatch(
                event.getTopic(), event.getMessage(), event.getUserId(), event.getMessageId()));
    }
}
