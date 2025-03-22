package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.lottery.domain.award.model.entity.TaskEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.TaskStateVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.persistent.dao.TaskMapper;
import com.lottery.infrastructure.persistent.dao.UserAwardRecordMapper;
import com.lottery.infrastructure.persistent.dao.UserOrderMapper;
import com.lottery.infrastructure.persistent.po.Task;
import com.lottery.infrastructure.persistent.po.UserAwardRecord;
import com.lottery.infrastructure.persistent.po.UserOrder;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author 永
 * 奖品服务仓储实现
 */
@Repository
@Slf4j
public class UserAwardRepositoryImpl implements UserAwardRepository {
    @Resource
    private UserAwardRecordMapper userAwardRecordMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        BeanUtils.copyProperties(userAwardRecordEntity, userAwardRecord);
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        Task task = new Task();
        BeanUtils.copyProperties(taskEntity, task);
        task.setMessage(String.valueOf(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userAwardRecordEntity.getUserId());
        userOrder.setActivityId(userAwardRecordEntity.getActivityId());

        //写入数据库
        try {
            dbRouter.doRouter(userAwardRecordEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    userAwardRecordMapper.insert(userAwardRecord);
                    taskMapper.insert(task);
                    //更新抽奖单
                    int count=userOrderMapper.updateUserOrderStateUsed(userOrder);
                    if (count!=1){
                        status.setRollbackOnly();
                        log.error("更新抽奖单失败,该抽奖单已被使用");
                        return new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(),ResponseCode.ACTIVITY_ORDER_ERROR.getMessage());
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录失败，唯一索引冲突 userId: {} activityId: {} awardId: {}", userAwardRecordEntity.getUserId(), userAwardRecordEntity.getActivityId(), userAwardRecordEntity.getAwardId(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getMessage());
                }
            });
        } finally {
            dbRouter.clear();
        }

        //发送mq消息
        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录，task 任务表
            taskMapper.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userAwardRecordEntity.getUserId(), task.getTopic());
            taskMapper.updateTaskSendMessageFail(task);
        }
    }
}
