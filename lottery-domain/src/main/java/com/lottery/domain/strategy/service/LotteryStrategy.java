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
}
