package com.lottery.infrastructure.adapter.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.rebate.model.aggregate.RebateAggregate;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.entity.TaskEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.model.valobj.RebateVO;
import com.lottery.domain.rebate.repository.RebateRepository;
import com.lottery.infrastructure.dao.BehaviorRebateMapper;
import com.lottery.infrastructure.dao.TaskMapper;
import com.lottery.infrastructure.dao.UserBehaviorRebateOrderMapper;
import com.lottery.infrastructure.dao.po.BehaviorRebate;
import com.lottery.infrastructure.dao.po.Task;
import com.lottery.infrastructure.dao.po.UserBehaviorRebateOrder;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 永
 * 返利领域仓储实现
 */
@Repository
@Slf4j
public class RebateRepositoryImpl implements RebateRepository {
    @Resource
    private BehaviorRebateMapper behaviorRebateMapper;
    @Resource
    private UserBehaviorRebateOrderMapper userBehaviorRebateOrderMapper;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private RedisService redisService;

    @Override
    public List<RebateVO> queryRebateConfig(BehaviorEntity behaviorEntity) {
        BehaviorTypeVO behaviorTypeVO = behaviorEntity.getBehaviorTypeVO();
        Long activityId = behaviorEntity.getActivityId();
        LambdaQueryWrapper<BehaviorRebate> queryWrapper = new QueryWrapper<BehaviorRebate>().lambda()
                .eq(BehaviorRebate::getBehaviorType, behaviorTypeVO.getCode())
                .eq(BehaviorRebate::getActivityId, activityId);
        List<BehaviorRebate> behaviorRebates = behaviorRebateMapper.selectList(queryWrapper);
        //构建vo
        return behaviorRebates.stream().map(behaviorRebate -> RebateVO.builder()
                .behaviorType(behaviorRebate.getBehaviorType())
                .rebateDesc(behaviorRebate.getRebateDesc())
                .behaviorRebateId(behaviorRebate.getId())
                .rebateType(behaviorRebate.getRebateType())
                .rebateConfig(behaviorRebate.getRebateConfig())
                .build()).toList();
    }

    @Override
    public void saveRebateAggregate(List<RebateAggregate> aggregates) {
        if (aggregates.isEmpty()) {
            log.warn("[RebateRepositoryImpl]该功能暂未配置");
            throw new AppException(ResponseCode.FEATURE_IS_NOT_CONFIGURED.getCode(), ResponseCode.FEATURE_IS_NOT_CONFIGURED.getMessage());
        }
        String userId = aggregates.get(0).getUserId();
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (RebateAggregate aggregate : aggregates) {
                        //保存返利单
                        RebateOrderEntity rebateOrderEntity = aggregate.getRebateOrderEntity();
                        //如果behaviorType是activity_gift，则说明是活动赠送的抽奖额度，只能领取一次，加入到redis中
                        if (BehaviorTypeVO.ACTIVITY_GIFT.getCode().equals(rebateOrderEntity.getBehaviorType())) {
                            log.info("[RebateRepositoryImpl]活动赠送的抽奖额度，只能领取一次，加入到redis中 activityId: {} behaviorRebateId: {} userId: {}",aggregate.getActivityId(),rebateOrderEntity.getBehaviorRebateId(), userId);
                            redisService.addToSet(Constants.RedisKey.IS_RECEIVE_GIFT + aggregate.getActivityId() + Constants.UNDERLINE + rebateOrderEntity.getBehaviorRebateId(), userId);
                        }
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        BeanUtils.copyProperties(rebateOrderEntity, userBehaviorRebateOrder);
                        userBehaviorRebateOrderMapper.insert(userBehaviorRebateOrder);
                        log.debug("[RebateRepositoryImpl]返利流水记录成功 userId: {}", userId);
                        //保存任务
                        TaskEntity taskEntity = aggregate.getTaskEntity();
                        Task task = new Task();
                        BeanUtils.copyProperties(taskEntity, task);
                        task.setMessage(String.valueOf(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        taskMapper.insert(task);
                        log.debug("[RebateRepositoryImpl]返利任务记录成功 userId: {}", userId);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.warn("[RebateRepositoryImpl]返利流水记录失败，唯一索引冲突 userId: {}", userId);
                    throw new DuplicateKeyException(Objects.requireNonNull(e.getMessage()));
                }
            });
        } finally {
            dbRouter.clear();
        }
        //发送mq消息 不用加在事务里，因为有事务补偿机制
        for (RebateAggregate aggregate : aggregates) {
            TaskEntity taskEntity = aggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                log.debug("[RebateRepositoryImpl]发送返利记录MQ消息成功 userId: {} topic: {}", userId, task.getTopic());
                //更新数据库
                taskMapper.updateTaskSendMessageCompleted(task);
                log.debug("[RebateRepositoryImpl]任务表状态成功 userId: {} topic: {}", userId, task.getTopic());
            } catch (Exception e) {
                log.error("[RebateRepositoryImpl]发送返利记录MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskMapper.updateTaskSendMessageFail(task);
            }
        }
    }

    @Override
    public List<RebateOrderEntity> queryRebateOrder(String userId, String outBusinessNo) {
        LambdaQueryWrapper<UserBehaviorRebateOrder> queryWrapper = new QueryWrapper<UserBehaviorRebateOrder>().lambda()
                .eq(UserBehaviorRebateOrder::getUserId, userId)
                .eq(UserBehaviorRebateOrder::getOutBusinessNo, outBusinessNo);
        List<UserBehaviorRebateOrder> userBehaviorRebateOrders;
        try {
            dbRouter.doRouter(userId);
            userBehaviorRebateOrders = userBehaviorRebateOrderMapper.selectList(queryWrapper);
        } finally {
            dbRouter.clear();
        }
        List<RebateOrderEntity> rebateOrderEntities = new ArrayList<>(userBehaviorRebateOrders.size());
        for (UserBehaviorRebateOrder userBehaviorRebateOrder : userBehaviorRebateOrders) {
            RebateOrderEntity rebateOrderEntity = new RebateOrderEntity();
            BeanUtils.copyProperties(userBehaviorRebateOrder, rebateOrderEntity);
            rebateOrderEntities.add(rebateOrderEntity);
        }
        return rebateOrderEntities;
    }

    @Override
    public boolean queryIsHaveRebateOrder(String userId, Long activityId, String outBusinessNo) {
        LambdaQueryWrapper<UserBehaviorRebateOrder> queryWrapper = new QueryWrapper<UserBehaviorRebateOrder>().lambda()
                .eq(UserBehaviorRebateOrder::getUserId, userId)
                .eq(UserBehaviorRebateOrder::getActivityId, activityId)
                .eq(UserBehaviorRebateOrder::getOutBusinessNo, outBusinessNo);
        List<UserBehaviorRebateOrder> userBehaviorRebateOrders;
        try {
            dbRouter.doRouter(userId);
            userBehaviorRebateOrders = userBehaviorRebateOrderMapper.selectList(queryWrapper);
        } finally {
            dbRouter.clear();
        }
        return !userBehaviorRebateOrders.isEmpty();
    }

    @Override
    public boolean isReceiveGift(String activityId, Long behaviorRebateId, String userId) {
        return redisService.isSetMember(Constants.RedisKey.IS_RECEIVE_GIFT + activityId + Constants.UNDERLINE + behaviorRebateId, userId);
    }

    @Override
    public RebateVO queryOneRebateConfig(BehaviorEntity behaviorEntity) {
        BehaviorTypeVO behaviorTypeVO = behaviorEntity.getBehaviorTypeVO();
        Long activityId = behaviorEntity.getActivityId();
        Long behaviorRebateId = behaviorEntity.getBehaviorRebateId();
        LambdaQueryWrapper<BehaviorRebate> queryWrapper = new QueryWrapper<BehaviorRebate>().lambda()
                .eq(BehaviorRebate::getBehaviorType, behaviorTypeVO.getCode())
                .eq(BehaviorRebate::getId, behaviorRebateId)
                .eq(BehaviorRebate::getActivityId, activityId);
        BehaviorRebate behaviorRebate = behaviorRebateMapper.selectOne(queryWrapper);
        //构建vo
        return RebateVO.builder()
                .behaviorType(behaviorRebate.getBehaviorType())
                .rebateDesc(behaviorRebate.getRebateDesc())
                .behaviorRebateId(behaviorRebate.getId())
                .rebateType(behaviorRebate.getRebateType())
                .rebateConfig(behaviorRebate.getRebateConfig())
                .build();
    }
}
