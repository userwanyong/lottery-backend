package com.lottery.domain.award.service.distribute.impl;

import com.lottery.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCreditAwardEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.domain.award.service.distribute.DistributeAward;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 永
 * 奖品领域-黑名单奖品
 */
@Component(Constants.AwardModel.RULE_BLACKLIST)
@Slf4j
public class UserBlackListAward implements DistributeAward {
    @Resource
    private UserAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Long awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (awardConfig == null || awardConfig.isEmpty()) {
            awardConfig = repository.queryAwardConfig(awardId);
        }
        BigDecimal creditAmount = new BigDecimal(awardConfig);
        log.info("[UserCreditRandomAward]黑名单用户「{}」获得积分值「{}」", distributeAwardEntity.getUserId(), creditAmount);
        // 构建集合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildUserAwardRecordEntity(distributeAwardEntity.getId(), distributeAwardEntity.getUserId(), distributeAwardEntity.getActivityId(), distributeAwardEntity.getUserOrderId(), awardId, AwardStateVO.complete);
        UserCreditAwardEntity userCreditAward = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), distributeAwardEntity.getActivityId(), creditAmount);
        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAward);
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        // 保存
        repository.saveGiveOutPrizes(giveOutPrizesAggregate);
    }
}
