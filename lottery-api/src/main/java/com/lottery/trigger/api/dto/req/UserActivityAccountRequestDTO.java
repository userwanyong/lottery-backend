package com.lottery.trigger.api.dto.req;

import lombok.Data;

/**
 * @author 永
 * 查询账户额度，入参
 */
@Data
public class UserActivityAccountRequestDTO {
    private String userId;
    private Long activityId;
}
