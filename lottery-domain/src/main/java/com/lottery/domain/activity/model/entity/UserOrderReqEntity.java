package com.lottery.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 永
 * 活动参与单请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
