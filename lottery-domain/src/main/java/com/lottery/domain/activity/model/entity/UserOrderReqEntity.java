package com.lottery.domain.activity.model.entity;

import lombok.Data;

/**
 * @author 永
 * 活动参与单请求体
 */
@Data
public class UserOrderReqEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
