package com.marketing.domain.strategy.service;

import com.marketing.domain.strategy.model.entity.LotteryResEntity;
import com.marketing.domain.strategy.model.entity.LotteryReqEntity;

/**
 * @author 永
 * 抽奖策略接口
 */
public interface LotteryStrategy {

    /**
     * @author 永
     * @param raffleFactorEntity 抽奖要素实体，根据入参信息计算抽奖结果
     * @return 抽奖的奖品
     */
    LotteryResEntity performRaffle(LotteryReqEntity raffleFactorEntity);
}
