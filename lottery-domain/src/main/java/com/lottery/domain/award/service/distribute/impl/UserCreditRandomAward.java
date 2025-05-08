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
import java.math.MathContext;

/**
 * @author 永
 * 奖品领域-积分发奖
 */
@Component(Constants.AwardModel.USER_CREDIT_RANDOM)
@Slf4j
public class UserCreditRandomAward implements DistributeAward {
    @Resource
    private UserAwardRepository repository;
    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Long awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (awardConfig==null||awardConfig.isEmpty()){
            awardConfig=repository.queryAwardConfig(awardId);
        }
        String[] split = awardConfig.split(",");
        if (split.length!=2){
            log.error("[UserCreditRandomAward]award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
        }
        // 生成随机积分
        BigDecimal creditAmount = generateRandom(new BigDecimal(split[0]), new BigDecimal(split[1]));
        // 构建集合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildUserAwardRecordEntity(distributeAwardEntity.getUserId(), distributeAwardEntity.getOrderId(), awardId, AwardStateVO.complete);
        UserCreditAwardEntity userCreditAward = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);
        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAward);
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        // 保存
        repository.saveGiveOutPrizes(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
