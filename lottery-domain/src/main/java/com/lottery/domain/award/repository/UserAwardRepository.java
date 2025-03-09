package com.lottery.domain.award.repository;

import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @author 永
 * 奖品发放仓储接口
 */
public interface UserAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
