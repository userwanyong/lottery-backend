package com.lottery.domain.award.model.aggregate;

import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCreditAwardEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 发奖聚合对象
 */
@Data
public class GiveOutPrizesAggregate {
    /** 用户ID */
    private String userId;
    /** 用户发奖记录 */
    private UserAwardRecordEntity userAwardRecordEntity;
    /** 用户积分奖品 */
    private UserCreditAwardEntity userCreditAwardEntity;

    public static UserAwardRecordEntity buildUserAwardRecordEntity(String userId, String orderId, Long awardId, AwardStateVO awardState) {
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardState(awardState);
        return userAwardRecord;
    }
    public static UserCreditAwardEntity buildUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        UserCreditAwardEntity userCreditAward = new UserCreditAwardEntity();
        userCreditAward.setUserId(userId);
        userCreditAward.setCreditAmount(creditAmount);
        return userCreditAward;
    }
}
