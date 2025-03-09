package com.lottery.domain.task.service;



import com.lottery.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author 永
 * 任务服务接口
 */
public interface TaskService {
    /**
     * 查询发送MQ失败和超时1分钟未发送的MQ
     *
     * @return 未发送的任务消息列表10条
     */
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
