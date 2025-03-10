package com.lottery.domain.task.repository;

import com.lottery.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author 永
 * 任务领域仓储接口
 */
public interface TaskRepository {
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
