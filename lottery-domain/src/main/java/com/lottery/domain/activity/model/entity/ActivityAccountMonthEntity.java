package com.lottery.domain.activity.model.entity;

import lombok.Data;

/**
 * @author 永
 */
@Data
public class ActivityAccountMonthEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 月（yyyy-mm）
     */
    private String month;

    /**
     * 月次数
     */
    private Integer monthCount;

    /**
     * 月次数-剩余
     */
    private Integer monthCountSurplus;
}
