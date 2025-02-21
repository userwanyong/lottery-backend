package com.lottery.domain.strategy.service.lottery;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;

/**
 * @author 永
 * 库存相关操作
 */
public interface Stock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     */
    LotteryReqEntity takeQueueValue();

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Long awardId);
}
