package com.lottery.domain.award.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 积分发奖实体
 */
@Data
public class UserCountAwardEntity {
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 次数 */
    private Integer count;
}
