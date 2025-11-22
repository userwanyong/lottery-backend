package com.lottery.infrastructure.adapter.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lottery.domain.award.model.aggregate.CountPrizesAggregate;
import com.lottery.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.lottery.domain.award.model.entity.TaskEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.entity.UserCountAwardEntity;
import com.lottery.domain.award.model.entity.UserCreditAwardEntity;
import com.lottery.domain.award.model.valobj.AccountStatusVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import com.lottery.domain.rebate.model.valobj.RebateTypeVO;
import com.lottery.infrastructure.dao.*;
import com.lottery.infrastructure.dao.po.*;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Resource
    private CreditRecordMapper creditRecordMapper;
    @Autowired
    private ActivityAccountDayMapper activityAccountDayMapper;
    @Resource
    private ActivityAccountMonthMapper activityAccountMonthMapper;
    @Resource
    private ActivityAccountMapper activityAccountMapper;
    @Resource
    private ActivityRecordMapper activityRecordMapper;

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        BeanUtils.copyProperties(userAwardRecordEntity, userAwardRecord);
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        Task task = new Task();
        BeanUtils.copyProperties(taskEntity, task);
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserOrder userOrder = new UserOrder();
        userOrder.setId(userAwardRecordEntity.getUserOrderId());
        userOrder.setUserId(userAwardRecordEntity.getUserId());
        userOrder.setActivityId(userAwardRecordEntity.getActivityId());
        //将中奖记录和任务写入数据库表，更新抽奖单状态为used已使用
        try {
            dbRouter.doRouter(userAwardRecordEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    userAwardRecordMapper.insert(userAwardRecord);
                    taskMapper.insert(task);
                    //更新抽奖单
                    int count = userOrderMapper.updateUserOrderStateUsed(userOrder);
                    if (count != 1) {
                        status.setRollbackOnly();
                        log.error("[UserAwardRepositoryImpl]更新抽奖单失败,该抽奖单已被使用 orderId: {}", userOrder.getId());
                        return new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(), ResponseCode.ACTIVITY_ORDER_ERROR.getMessage());
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("[UserAwardRepositoryImpl]写入中奖记录失败，唯一索引冲突 userId: {} activityId: {} awardId: {}", userAwardRecordEntity.getUserId(), userAwardRecordEntity.getActivityId(), userAwardRecordEntity.getAwardId(), e);
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
            // 更新数据库记录，task 任务表 状态为 completed 已完成
            taskMapper.updateTaskSendMessageCompleted(task);
            log.debug("[UserAwardRepositoryImpl]写入中奖记录，MQ消息发送成功 userId: {} topic: {}", userAwardRecordEntity.getUserId(), task.getTopic());
        } catch (Exception e) {
            log.error("[UserAwardRepositoryImpl]写入中奖记录，MQ消息发送失败 userId: {} topic: {}", userAwardRecordEntity.getUserId(), task.getTopic());
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
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());
        // 更新用户积分
        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setUserId(userId);
        creditAccount.setActivityId(userAwardRecordEntity.getActivityId());
        creditAccount.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        creditAccount.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        creditAccount.setAccountStatus(AccountStatusVO.open.getCode());
        // 写入积分记录
        CreditRecord creditRecord = new CreditRecord();
        creditRecord.setUserId(userId);
        creditRecord.setActivityId(userAwardRecordEntity.getActivityId());
        creditRecord.setTradeName(TradeNameVO.LOTTERY_AWARD.getName());
        creditRecord.setTradeType(TradeTypeVO.FORWARD.getCode());
        creditRecord.setTradeAmount(userCreditAwardEntity.getCreditAmount());
        // 可重复抽取到积分，所以精确到秒
        creditRecord.setOutBusinessNo(userId+Constants.UNDERLINE+ RebateTypeVO.LOTTERY.getCode()+Constants.UNDERLINE +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 写入积分记录表
                    creditRecordMapper.insert(creditRecord);
                    log.debug("[UserAwardRepositoryImpl]写入积分记录成功 userId:{}", userId);
                    // 更新/创建积分账户
                    LambdaQueryWrapper<CreditAccount> queryWrapper = new QueryWrapper<CreditAccount>().lambda().eq(CreditAccount::getUserId, userId).eq(CreditAccount::getActivityId, userAwardRecordEntity.getActivityId());
                    CreditAccount dbCreditAccount = creditAccountMapper.selectOne(queryWrapper);
                    if (dbCreditAccount == null) {
                        // 新增
                        creditAccountMapper.insert(creditAccount);
                        log.debug("[UserAwardRepositoryImpl]创建积分账户成功 userId:{}", userId);
                    } else {
                        // 更新
                        creditAccountMapper.update(creditAccount);
                        log.debug("[UserAwardRepositoryImpl]更新积分账户成功 userId:{}", userId);
                    }
                    // 更新中奖记录状态为completed 发奖完成
                    int count = userAwardRecordMapper.update(userAwardRecord, new LambdaUpdateWrapper<UserAwardRecord>().eq(UserAwardRecord::getUserOrderId, userAwardRecordEntity.getUserOrderId()));
                    log.debug("[UserAwardRepositoryImpl]更新中奖记录状态为 completed 发奖完成成功 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                    if (count == 0) {
                        log.error("[UserAwardRepositoryImpl]更新中奖记录状态为 completed 发奖完成失败 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        status.setRollbackOnly();
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("[UserAwardRepositoryImpl]更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }

    }

    @Override
    public String queryAwardKey(Long awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new QueryWrapper<Award>().lambda()
                .eq(Award::getId, awardId);
        Award award = awardMapper.selectOne(queryWrapper);
        return award.getAwardKey();
    }

    @Override
    public String queryAwardConfig(Long awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new QueryWrapper<Award>().lambda()
                .eq(Award::getId, awardId);
        Award award = awardMapper.selectOne(queryWrapper);
        return award.getAwardConfig();
    }

    @Override
    public void saveCountPrizes(CountPrizesAggregate countPrizesAggregate) {
        String userId = countPrizesAggregate.getUserId();
        UserAwardRecordEntity userAwardRecordEntity = countPrizesAggregate.getUserAwardRecordEntity();
        UserCountAwardEntity userCountAwardEntity = countPrizesAggregate.getUserCountAwardEntity();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        // 添加抽奖次数流水
        ActivityRecord activityRecord = new ActivityRecord();
        activityRecord.setUserId(userId);
        activityRecord.setActivityId(userCountAwardEntity.getActivityId());
        activityRecord.setTotalCount(userCountAwardEntity.getCount());
        activityRecord.setDayCount(userCountAwardEntity.getCount());
        activityRecord.setMonthCount(userCountAwardEntity.getCount());
        activityRecord.setPayAmount(BigDecimal.ZERO);
        activityRecord.setState(userAwardRecordEntity.getAwardState().getCode());
        activityRecord.setOutBusinessNo(userId+Constants.UNDERLINE+ RebateTypeVO.COUNT.getCode()+Constants.UNDERLINE +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 既然能抽奖肯定已经有抽奖账户了，直接更新三个抽奖账户即可
                    LambdaQueryWrapper<ActivityAccountDay> activityAccountDayLambdaQueryWrapper = new LambdaQueryWrapper<ActivityAccountDay>()
                            .eq(ActivityAccountDay::getUserId, userId)
                            .eq(ActivityAccountDay::getActivityId, userCountAwardEntity.getActivityId())
                            .eq(ActivityAccountDay::getDay,dateFormatDay.format(new Date()));
                    ActivityAccountDay activityAccountDay = activityAccountDayMapper.selectOne(activityAccountDayLambdaQueryWrapper);
                    activityAccountDay.setDayCountSurplus(activityAccountDay.getDayCountSurplus() + userCountAwardEntity.getCount());
                    activityAccountDay.setDayCount(activityAccountDay.getDayCount() + userCountAwardEntity.getCount());
                    activityAccountDayMapper.updateById(activityAccountDay);
                    log.debug("[UserAwardRepositoryImpl]更新日账户成功 userId:{}", userId);
                    LambdaQueryWrapper<ActivityAccountMonth> activityAccountMonthLambdaQueryWrapper = new LambdaQueryWrapper<ActivityAccountMonth>()
                            .eq(ActivityAccountMonth::getUserId, userId)
                            .eq(ActivityAccountMonth::getActivityId, userCountAwardEntity.getActivityId())
                            .eq(ActivityAccountMonth::getMonth, dateFormatMonth.format(new Date()));
                    ActivityAccountMonth activityAccountMonth = activityAccountMonthMapper.selectOne(activityAccountMonthLambdaQueryWrapper);
                    activityAccountMonth.setMonthCountSurplus(activityAccountMonth.getMonthCountSurplus() + userCountAwardEntity.getCount());
                    activityAccountMonth.setMonthCount(activityAccountMonth.getMonthCount() + userCountAwardEntity.getCount());
                    activityAccountMonthMapper.updateById(activityAccountMonth);
                    log.debug("[UserAwardRepositoryImpl]更新月账户成功 userId:{}", userId);
                    LambdaQueryWrapper<ActivityAccount> activityAccountLambdaQueryWrapper = new LambdaQueryWrapper<ActivityAccount>()
                            .eq(ActivityAccount::getUserId, userId)
                            .eq(ActivityAccount::getActivityId, userCountAwardEntity.getActivityId());
                    ActivityAccount activityAccount = activityAccountMapper.selectOne(activityAccountLambdaQueryWrapper);
                    activityAccount.setTotalCountSurplus(activityAccount.getTotalCountSurplus() + userCountAwardEntity.getCount());
                    activityAccount.setTotalCount(activityAccount.getTotalCount() + userCountAwardEntity.getCount());
                    activityAccount.setDayCount(activityAccount.getDayCount() + userCountAwardEntity.getCount());
                    activityAccount.setMonthCount(activityAccount.getMonthCount() + userCountAwardEntity.getCount());
                    activityAccount.setDayCountSurplus(activityAccount.getDayCountSurplus() + userCountAwardEntity.getCount());
                    activityAccount.setMonthCountSurplus(activityAccount.getMonthCountSurplus() + userCountAwardEntity.getCount());
                    activityAccountMapper.updateById(activityAccount);
                    log.debug("[UserAwardRepositoryImpl]更新总账户成功 userId:{}", userId);

                    // 记录抽奖次数流水
                    activityRecordMapper.insert(activityRecord);

                    // 更新中奖记录状态为completed 发奖完成
                    int count = userAwardRecordMapper.update(userAwardRecord, new LambdaUpdateWrapper<UserAwardRecord>().eq(UserAwardRecord::getUserOrderId, userAwardRecordEntity.getUserOrderId()));
                    log.debug("[UserAwardRepositoryImpl]更新中奖记录状态为 completed 发奖完成成功 userId:{}", userId);
                    if (count == 0) {
                        log.error("[UserAwardRepositoryImpl]更新中奖记录状态为 completed 发奖完成失败 userId:{}", userId);
                        status.setRollbackOnly();
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("[UserAwardRepositoryImpl]更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }

    }
}
