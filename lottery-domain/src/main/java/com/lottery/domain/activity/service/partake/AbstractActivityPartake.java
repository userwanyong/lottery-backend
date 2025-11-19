package com.lottery.domain.activity.service.partake;

import com.lottery.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.PartakeOrderReqEntity;
import com.lottery.domain.activity.model.entity.PartakeOrderResEntity;
import com.lottery.domain.activity.model.valobj.ActivityStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.ActivityPartakeService;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;

import java.util.Date;

/**
 * @author 永
 * 活动-参与领域-抽象类
 */
public abstract class AbstractActivityPartake implements ActivityPartakeService {

    protected ActivityRepository activityRepository;


    public AbstractActivityPartake(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public PartakeOrderResEntity createPartakeOrder(String userId, Long activityId) {
        return createPartakeOrder(PartakeOrderReqEntity.builder().userId(userId).activityId(activityId).build());
    }

    @Override
    public PartakeOrderResEntity createPartakeOrder(PartakeOrderReqEntity reqEntity) {
        //基础信息
        String userId = reqEntity.getUserId();
        Long activityId = reqEntity.getActivityId();
        Date currentTime = new Date();
        //查询活动
        ActivityEntity activityEntity = activityRepository.queryActivityByActivityId(activityId);
        //判断活动是否开启、是否在活动时间
        if (!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getMessage());
        }
        if (currentTime.before(activityEntity.getBeginDateTime()) || currentTime.after(activityEntity.getEndDateTime())) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getMessage());
        }
        //查询是否有抽奖单但未被消费,有的话直接返回
        PartakeOrderResEntity partakeOrderResEntity = activityRepository.queryNoUsedPartakeOrder(reqEntity);
        if (partakeOrderResEntity != null) {
            return partakeOrderResEntity;
        }
        //构建抽奖单
        PartakeOrderResEntity userOrderRes = this.buildUserPartakeOrder(userId, activityId, currentTime);
        //构建聚合对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentTime);
        createPartakeOrderAggregate.setPartakeOrderResEntity(userOrderRes);
        //保存聚合对象
        Long userOrderId =activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);
        userOrderRes.setId(userOrderId);
        return userOrderRes;
    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentTime);

    protected abstract PartakeOrderResEntity buildUserPartakeOrder(String userId, Long activityId, Date currentTime);
}
