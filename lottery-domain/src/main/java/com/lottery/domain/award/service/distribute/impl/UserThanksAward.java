package com.lottery.domain.award.service.distribute.impl;

import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.service.distribute.DistributeAward;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 奖品领域-黑名单奖品
 */
@Component(Constants.AwardModel.THANKS)
@Slf4j
public class UserThanksAward implements DistributeAward {

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        log.info("[UserThanksAward]参与奖，无需发放");
    }
}
