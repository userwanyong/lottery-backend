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

    List<StrategyAwardEntity> queryLotteryAwardListByActivityId(Long activityId);

    LotteryResEntity doLottery(LotteryReqEntity lotteryFactorEntity);
}
