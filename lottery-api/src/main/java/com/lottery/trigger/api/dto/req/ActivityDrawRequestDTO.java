package com.lottery.trigger.api.dto.req;

import lombok.Data;

/**
 * @author 永
 * 活动抽奖请求对象
 */
@Data
public class ActivityDrawRequestDTO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
}
