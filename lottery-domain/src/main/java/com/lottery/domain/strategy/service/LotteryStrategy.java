package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;

/**
 * @author 永
 * 抽奖策略接口
 */
public interface LotteryStrategy {

    /**
     * @param lotteryFactorEntity 抽奖要素实体，根据入参信息计算抽奖结果
     * @return 抽奖的奖品
     */
    LotteryResEntity performLottery(LotteryReqEntity lotteryFactorEntity);

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
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
