package com.lottery.trigger.api.dto.req;

import lombok.Data;

/**
 * @author 永
 * 执行抽奖，入参
 */
@Data
public class LotteryRequestDTO {
    // 抽奖策略ID
    private Long strategyId;
}
