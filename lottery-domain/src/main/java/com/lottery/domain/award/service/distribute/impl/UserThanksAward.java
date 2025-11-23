package com.lottery.domain.award.service.distribute.impl;

import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
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
@Component(Constants.AwardModel.THANKS)
@Slf4j
public class UserThanksAward implements DistributeAward {
    @Resource
    private UserAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        log.info("[UserThanksAward]参与奖，无需发放");
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setId(distributeAwardEntity.getId());
        userAwardRecord.setActivityId(distributeAwardEntity.getActivityId());
        userAwardRecord.setUserId(distributeAwardEntity.getUserId());
        userAwardRecord.setUserOrderId(distributeAwardEntity.getUserOrderId());
        userAwardRecord.setAwardId(distributeAwardEntity.getAwardId());
        userAwardRecord.setAwardState(AwardStateVO.complete);
        repository.saveThanksPrizes(userAwardRecord);
    }
}
