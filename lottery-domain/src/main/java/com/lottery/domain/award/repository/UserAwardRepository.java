package com.lottery.domain.award.repository;

import com.lottery.domain.award.model.aggregate.CountPrizesAggregate;
import com.lottery.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author 永
 * 奖品领域仓储接口
 */
public interface UserAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    void saveGiveOutPrizes(GiveOutPrizesAggregate giveOutPrizesAggregate);

    String queryAwardKey(Long awardId);

    String queryAwardConfig(Long awardId);

    void saveCountPrizes(CountPrizesAggregate countPrizesAggregate);

    void saveThanksPrizes(UserAwardRecordEntity userAwardRecord);
}
