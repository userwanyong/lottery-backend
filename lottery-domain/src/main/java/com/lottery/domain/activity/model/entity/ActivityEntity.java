package com.lottery.domain.activity.model.entity;

import com.lottery.domain.activity.model.valobj.ActivityStateVO;
import lombok.Data;

import java.util.Date;

/**
 * @author 永
 * 活动实体对象
 */
@Data
public class ActivityEntity {
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
    private ActivityStateVO state;

}
