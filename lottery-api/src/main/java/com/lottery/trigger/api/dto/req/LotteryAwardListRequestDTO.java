package com.lottery.trigger.api.dto.req;

import lombok.Data;

/**
 * @author 永
 * 查询抽奖奖品列表，入参
 */
@Data
public class LotteryAwardListRequestDTO {
    // 抽奖策略ID
    private Long strategyId;
}
