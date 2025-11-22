package com.lottery.domain.award.model.aggregate;

import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCountAwardEntity;
import com.lottery.domain.award.model.entity.UserCreditAwardEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 发奖聚合对象
 */
@Data
public class CountPrizesAggregate {
    /** 用户ID */
    private String userId;
    /** 用户发奖记录 */
    private UserAwardRecordEntity userAwardRecordEntity;
    /** 抽奖次数奖品 */
    private UserCountAwardEntity userCountAwardEntity;

    public static UserAwardRecordEntity buildUserAwardRecordEntity(Long id,String userId,Long activityId, Long orderId, Long awardId, AwardStateVO awardState) {
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setId(id);
        userAwardRecord.setActivityId(activityId);
        userAwardRecord.setUserId(userId);
        userAwardRecord.setUserOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardState(awardState);
        return userAwardRecord;
    }
    public static UserCountAwardEntity buildUserCountAwardEntity(String userId, Long activityId,Integer count) {
        UserCountAwardEntity userCountAwardEntity = new UserCountAwardEntity();
        userCountAwardEntity.setActivityId(activityId);
        userCountAwardEntity.setUserId(userId);
        userCountAwardEntity.setCount(count);
        return userCountAwardEntity;
    }
}
