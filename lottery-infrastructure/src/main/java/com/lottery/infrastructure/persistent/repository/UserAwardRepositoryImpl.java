package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lottery.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.lottery.domain.award.model.entity.TaskEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCreditAwardEntity;
import com.lottery.domain.award.model.valobj.AccountStatusVO;
import com.lottery.domain.award.model.valobj.TaskStateVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.persistent.dao.*;
import com.lottery.infrastructure.persistent.po.*;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
    private CreditAccountMapper creditAccountMapper;
    @Resource
    private AwardMapper awardMapper;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private RedisService redisService;

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
            eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
            // 更新数据库记录，task 任务表
            taskMapper.updateTaskSendMessageCompleted(task);
            log.info("写入中奖记录，发送MQ消息成功 userId: {} topic: {}", userAwardRecordEntity.getUserId(), task.getTopic());
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userAwardRecordEntity.getUserId(), task.getTopic());
            taskMapper.updateTaskSendMessageFail(task);
        }
    }

    @Override
    public void saveGiveOutPrizes(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();
        // 更新发奖状态
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        // 更新用户积分 「首次则插入数据」
        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setUserId(userId);
        creditAccount.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        creditAccount.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        creditAccount.setAccountStatus(AccountStatusVO.open.getCode());
        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 更新/创建积分账户
                    LambdaQueryWrapper<CreditAccount> queryWrapper = new QueryWrapper<CreditAccount>().lambda().eq(CreditAccount::getUserId, userId);
                    CreditAccount dbCreditAccount = creditAccountMapper.selectOne(queryWrapper);
                    if (dbCreditAccount==null){
                        // 新增
                        creditAccountMapper.insert(creditAccount);
                    }else {
                        // 更新
                        creditAccountMapper.update(creditAccount);
                    }
                    // 更新中奖记录
                    int count = userAwardRecordMapper.update(userAwardRecord, new LambdaUpdateWrapper<UserAwardRecord>().eq(UserAwardRecord::getUserId, userId).eq(UserAwardRecord::getOrderId, userAwardRecordEntity.getOrderId()));
                    if (count==0){
                        log.error("更新中奖记录，失败 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        status.setRollbackOnly();
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        }finally {
            dbRouter.clear();
            lock.unlock();
        }

    }

    @Override
    public String queryAwardKey(Long awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new QueryWrapper<Award>().lambda()
                .eq(Award::getAwardId, awardId);
        Award award = awardMapper.selectOne(queryWrapper);
        return award.getAwardKey();
    }

    @Override
    public String queryAwardConfig(Long awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new QueryWrapper<Award>().lambda()
                .eq(Award::getAwardId, awardId);
        Award award = awardMapper.selectOne(queryWrapper);
        return award.getAwardConfig();
    }
}
