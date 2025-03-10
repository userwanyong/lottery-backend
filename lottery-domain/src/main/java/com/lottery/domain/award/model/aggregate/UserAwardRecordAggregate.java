package com.lottery.domain.award.model.aggregate;

import com.lottery.domain.award.model.entity.TaskEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import lombok.Data;

/**
 * @author 永
 * 奖品领域聚合对象
 */
@Data
public class UserAwardRecordAggregate {
    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
