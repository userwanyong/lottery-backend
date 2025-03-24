package com.lottery.domain.award.service.distribute;

import com.lottery.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author 永
 * 奖品领域-发奖接口
 */
public interface DistributeAward {
    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
