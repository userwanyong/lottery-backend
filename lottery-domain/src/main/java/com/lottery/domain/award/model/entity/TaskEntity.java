package com.lottery.domain.award.model.entity;

import com.lottery.domain.award.event.SendAwardMessageEvent;
import com.lottery.domain.award.model.valobj.TaskStateVO;
import com.lottery.types.event.BaseEvent;
import lombok.Data;

/**
 * @author 永
 */
@Data
public class TaskEntity {
    /**
     * 消息主题
     */
    private String topic;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息主体
     */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;

    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private TaskStateVO state;
}
