package com.lottery.trigger.job;

import com.lottery.domain.task.model.entity.TaskEntity;
import com.lottery.domain.task.service.TaskService;
import com.lottery.trigger.config.MessageDispatcher;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 永
 * 任务表消息补偿（轻量版）：扫描因崩溃/失败遗留的 task，重新分发。
 * 正常路径由 LocalMessageEventListener 在事务提交后即时异步分发（等同原 MQ 准实时），
 * 本 Job 仅作最终一致性兜底——进程崩溃或即时分发失败时捞回。
 */
@Slf4j
@Component
public class SendMessageTaskJob {
    @Resource
    private TaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private MessageDispatcher messageDispatcher;

    @Scheduled(fixedDelay = 120000)
    @Timed(value = "sendMessageTaskJob", description = "任务表消息补偿")
    public void exec() {
        // 分布式锁：多机部署互备，抢占执行
        RLock lock = redissonClient.getLock("lottery-SendMessageTaskJob");
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) {
                return;
            }

            // 扫描遗留 task（即时分发失败或进程崩溃留下的 create/fail 状态）
            List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
            if (taskEntities.isEmpty()) {
                return;
            }
            for (TaskEntity taskEntity : taskEntities) {
                executor.execute(() -> messageDispatcher.dispatch(
                        taskEntity.getTopic(), taskEntity.getMessage(),
                        taskEntity.getUserId(), taskEntity.getMessageId()));
                log.info("【定时任务】补偿分发遗留任务 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
            }
        } catch (Exception e) {
            log.error("【定时任务】扫描任务表补偿分发失败", e);
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }
}
