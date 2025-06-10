package com.lottery.infrastructure.es.po;

import lombok.Data;

import java.util.Date;

/**
 * 任务表，发送MQ
 *
 * @author 永
 * @TableName task
 */
@Data
public class EsTask {
    /**
     * 自增ID
     */
    private Integer id;

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
    private String message;

    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private String state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}