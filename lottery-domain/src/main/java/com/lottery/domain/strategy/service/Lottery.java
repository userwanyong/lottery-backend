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
     * @param lotteryFactorEntity 抽奖要素实体，根据入参信息计算抽奖结果
     * @return 抽奖的奖品
     */
    LotteryResEntity performLottery(LotteryReqEntity lotteryFactorEntity);

}
