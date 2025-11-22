package com.lottery.domain.award.service.distribute.impl;

import com.lottery.domain.award.model.aggregate.CountPrizesAggregate;
import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCountAwardEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.domain.award.service.distribute.DistributeAward;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 奖品领域-黑名单奖品
 */
@Component(Constants.AwardModel.USER_CJ)
@Slf4j
public class UserCJAward implements DistributeAward {

    @Resource
    private UserAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        log.info("[UserCJAward]用户「{}」获得抽奖次数「{}」", distributeAwardEntity.getUserId(), distributeAwardEntity.getAwardConfig());
        UserCountAwardEntity userCountAwardEntity = CountPrizesAggregate
                .buildUserCountAwardEntity(distributeAwardEntity.getUserId(),
                        distributeAwardEntity.getActivityId(),
                        Integer.valueOf(distributeAwardEntity.getAwardConfig()));
        UserAwardRecordEntity userAwardRecordEntity = CountPrizesAggregate
                .buildUserAwardRecordEntity(distributeAwardEntity.getId(),
                        distributeAwardEntity.getUserId(),
                        distributeAwardEntity.getActivityId(),
                        distributeAwardEntity.getUserOrderId(),
                        distributeAwardEntity.getAwardId(),
                        AwardStateVO.complete);
        CountPrizesAggregate countPrizesAggregate = new CountPrizesAggregate();
        countPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        countPrizesAggregate.setUserCountAwardEntity(userCountAwardEntity);
        countPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        repository.saveCountPrizes(countPrizesAggregate);
    }
}
