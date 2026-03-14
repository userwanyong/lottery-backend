package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;

import java.util.List;

/**
 * @author 永
 * 策略-抽奖领域-库存相关操作
 */
public interface Stock {

    LotteryReqEntity takeQueueValue(String activityAward);

    void updateActivityAwardStock(Long activityId, Long awardId);

    List<String> getActivityAwardList();

    void clearAwardStock(String activityAward);

    void clearQueueValue(String activityAward);
}
