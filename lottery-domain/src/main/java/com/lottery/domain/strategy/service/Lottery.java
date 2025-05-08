package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author 永
 * 策略-抽奖领域-抽奖相关操作
 */
public interface Lottery {

    /**
     * 根据策略ID查询抽奖奖品列表配置
     *
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryLotteryAwardList(Long strategyId);
    /**
     * 根据策略ID查询抽奖奖品列表配置
     *
     * @param activityId 活动ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryLotteryAwardListByActivityId(Long activityId);

    /**
     * @param lotteryFactorEntity 抽奖请求实体
     * @return 抽到的奖品实体
     */
    LotteryResEntity performLottery(LotteryReqEntity lotteryFactorEntity);

}
