package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.rebate.model.aggregate.RebateAggregate;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.entity.TaskEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.model.valobj.RebateVO;
import com.lottery.domain.rebate.repository.RebateRepository;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.persistent.dao.BehaviorRebateMapper;
import com.lottery.infrastructure.persistent.dao.TaskMapper;
import com.lottery.infrastructure.persistent.dao.UserBehaviorRebateOrderMapper;
import com.lottery.infrastructure.persistent.po.BehaviorRebate;
import com.lottery.infrastructure.persistent.po.Task;
import com.lottery.infrastructure.persistent.po.UserBehaviorRebateOrder;
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

    @Override
    public List<RebateVO> queryRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        LambdaQueryWrapper<BehaviorRebate> queryWrapper = new QueryWrapper<BehaviorRebate>().lambda()
                .eq(BehaviorRebate::getBehaviorType, behaviorTypeVO.getCode());
        List<BehaviorRebate> behaviorRebates = behaviorRebateMapper.selectList(queryWrapper);
        //构建vo
        return behaviorRebates.stream().map(behaviorRebate -> RebateVO.builder()
                .behaviorType(behaviorRebate.getBehaviorType())
                .rebateDesc(behaviorRebate.getRebateDesc())
                .rebateType(behaviorRebate.getRebateType())
                .rebateConfig(behaviorRebate.getRebateConfig())
                .build()).toList();
    }

    @Override
    public void saveRebateAggregate(List<RebateAggregate> aggregates) {
        String userId = aggregates.get(0).getUserId();
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (RebateAggregate aggregate : aggregates) {
                        //保存返利单
                        RebateOrderEntity rebateOrderEntity = aggregate.getRebateOrderEntity();
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
                    log.error("[RebateRepositoryImpl]返利流水记录失败，唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getMessage());
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
    public boolean queryIsHaveRebateOrder(String userId, String outBusinessNo) {
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
        return !userBehaviorRebateOrders.isEmpty();
    }
}
