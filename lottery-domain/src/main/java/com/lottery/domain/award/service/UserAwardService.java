package com.lottery.domain.award.service;

import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author 永
 * 奖品领域接口
 */
public interface UserAwardService {
    /**
     * 保存奖品发放记录
     */
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
    /**
     * 发奖
     */
    void distributeAward(DistributeAwardEntity distributeAwardEntity);
}
