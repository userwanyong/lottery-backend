package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;

import java.util.List;

/**
 * @author 永
 * 策略-抽奖领域-库存相关操作
 */
public interface Stock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     */
    LotteryReqEntity takeQueueValue(String strategyAward);

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Long awardId);

    List<String> getStrategyAwardList();

    void clearAwardStock(String strategyAward);

    void clearQueueValue(String strategyAward);
}
