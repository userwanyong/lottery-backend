package com.lottery.domain.activity.model.entity;

import lombok.Data;

/**
 * @author 永
 * 活动次数实体对象
 */
@Data
public class ActivityCountEntity {
    /**
     * 活动次数编号
     */
    private Long activityCountId;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

}
