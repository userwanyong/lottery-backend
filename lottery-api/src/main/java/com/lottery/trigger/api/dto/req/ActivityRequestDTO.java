package com.lottery.trigger.api.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖活动表
 *
 * @author 永
 * @TableName activity
 */
@Data
public class ActivityRequestDTO {

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private Date beginDateTime;

    /**
     * 结束时间
     */
    private Date endDateTime;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 活动状态
     */
    private String state;

}