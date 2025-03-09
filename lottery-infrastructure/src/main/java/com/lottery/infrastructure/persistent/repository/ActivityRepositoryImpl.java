package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lottery.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.lottery.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.model.valobj.ActivityStateVO;
import com.lottery.domain.activity.model.valobj.UserOrderStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.persistent.dao.*;
import com.lottery.infrastructure.persistent.po.*;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 永
 * 活动仓储服务
 */
@Repository
@Slf4j
public class ActivityRepositoryImpl implements ActivityRepository {
    @Resource
    private RedisService redisService;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ActivitySkuMapper activitySkuMapper;
    @Resource
    private ActivityCountMapper activityCountMapper;
    @Resource
    private ActivityOrderMapper activityOrderMapper;
    @Resource
    private ActivityAccountMapper activityAccountMapper;
    @Resource
    private ActivityAccountMonthMapper activityAccountMonthMapper;
    @Resource
    private ActivityAccountDayMapper activityAccountDayMapper;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;


    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        LambdaQueryWrapper<ActivitySku> queryWrapper = new QueryWrapper<ActivitySku>().lambda().eq(ActivitySku::getSku, sku);
        ActivitySku activitySku = activitySkuMapper.selectOne(queryWrapper);
        ActivitySkuEntity activitySkuEntity = new ActivitySkuEntity();
        BeanUtils.copyProperties(activitySku, activitySkuEntity);
        return activitySkuEntity;
    }

    @Override
    public ActivityEntity queryActivityByActivityId(Long activityId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) {
            return activityEntity;
        }
        // 从库中获取数据
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda().eq(Activity::getActivityId, activityId);
        Activity activity = activityMapper.selectOne(queryWrapper);
        ActivityEntity dbActivityEntity = new ActivityEntity();
        BeanUtils.copyProperties(activity, dbActivityEntity);
        dbActivityEntity.setState(ActivityStateVO.valueOf(activity.getState()));
        redisService.setValue(cacheKey, dbActivityEntity);
        return dbActivityEntity;
    }

    @Override
    public ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (activityCountEntity != null) {
            return activityCountEntity;
        }
        // 从库中获取数据
        LambdaQueryWrapper<ActivityCount> queryWrapper = new QueryWrapper<ActivityCount>().lambda().eq(ActivityCount::getActivityCountId, activityCountId);
        ActivityCount activityCount = activityCountMapper.selectOne(queryWrapper);
        ActivityCountEntity dbActivityCountEntity = new ActivityCountEntity();
        BeanUtils.copyProperties(activityCount, dbActivityCountEntity);
        redisService.setValue(cacheKey, dbActivityCountEntity);
        return dbActivityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        try {
            // 订单对象
            ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
            ActivityOrder activityOrder = new ActivityOrder();
            activityOrder.setUserId(activityOrderEntity.getUserId());
            activityOrder.setSku(activityOrderEntity.getSku());
            activityOrder.setActivityId(activityOrderEntity.getActivityId());
            activityOrder.setActivityName(activityOrderEntity.getActivityName());
            activityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            activityOrder.setOrderId(activityOrderEntity.getOrderId());
            activityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            activityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            activityOrder.setDayCount(activityOrderEntity.getDayCount());
            activityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            activityOrder.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            activityOrder.setDayCount(createQuotaOrderAggregate.getDayCount());
            activityOrder.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            activityOrder.setState(activityOrderEntity.getState().getCode());
            activityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            // 账户对象
            ActivityAccount activityAccount = new ActivityAccount();
            BeanUtils.copyProperties(activityOrderEntity, activityAccount);
            activityAccount.setTotalCountSurplus(activityOrderEntity.getTotalCount());
            activityAccount.setDayCountSurplus(activityOrderEntity.getDayCount());
            activityAccount.setMonthCountSurplus(activityOrderEntity.getMonthCount());

            // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(createQuotaOrderAggregate.getUserId());
            // 编程式事务
            transactionTemplate.execute(status -> {
                try {
                    // 1. 写入订单
                    activityOrderMapper.insert(activityOrder);
                    // 2. 更新账户
                    int count = activityAccountMapper.updateAccount(activityAccount);
                    // 3. 创建账户 - 更新为0，则账户不存在，创新新账户。
                    if (0 == count) {
                        activityAccountMapper.insert(activityAccount);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {//发生唯一索引冲突异常时
                    status.setRollbackOnly(); //标记当前事务为回滚状态
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public void cacheActivitySkuStockCount(String key, Integer stockCountSurplus) {
        if (redisService.isExists(key)) {
            return;
        }
        redisService.setAtomicLong(key, stockCountSurplus);
    }

    @Override
    public boolean reduceActivitySkuStock(Long sku,String key, Date endDateTime) {
        long count = redisService.decr(key);
        if (count == 0) {
            // 库存消耗没了以后，发送MQ消息，更新数据库库存
            eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(), activitySkuStockZeroMessageEvent.buildEventMessage(sku));
        } else if (count < 0) {
            redisService.setAtomicLong(key, 0);
            return false;
        }
        String lockKey = key + Constants.UNDERLINE + count;
        //过期时间为活动结束后一天
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey,expireMillis,TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        LambdaUpdateWrapper<ActivitySku> queryWrapper = new LambdaUpdateWrapper<ActivitySku>()
                .setSql("stock_count_surplus = stock_count_surplus - 1")
                .set(ActivitySku::getUpdateTime, new Date())
                .eq(ActivitySku::getSku, sku)
                .gt(ActivitySku::getStockCountSurplus, 0);
        activitySkuMapper.update(null, queryWrapper);
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        LambdaUpdateWrapper<ActivitySku> queryWrapper = new LambdaUpdateWrapper<ActivitySku>()
                .set(ActivitySku::getStockCountSurplus, 0)
                .set(ActivitySku::getUpdateTime, new Date())
                .eq(ActivitySku::getSku, sku);
        activitySkuMapper.update(null, queryWrapper);
    }

    @Override
    public UserOrderResEntity queryNoUsedPartakeOrder(UserOrderReqEntity reqEntity) {
        UserOrder userOrder = new UserOrder();
        BeanUtils.copyProperties(reqEntity, userOrder);
        UserOrder order = userOrderMapper.queryNoUsedPartakeOrder(userOrder);
        if (order == null){
            return null;
        }
        UserOrderResEntity userOrderResEntity = new UserOrderResEntity();
        BeanUtils.copyProperties(order, userOrderResEntity);
        userOrderResEntity.setOrderState(UserOrderStateVO.valueOf(order.getOrderState()));
        return userOrderResEntity;
    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        ActivityAccount activityAccount = new ActivityAccount();
        activityAccount.setActivityId(activityId);
        activityAccount.setUserId(userId);
        ActivityAccount dbActivityAccount = activityAccountMapper.queryActivityAccountByUserId(activityAccount);
        if (dbActivityAccount == null){
            return null;
        }
        ActivityAccountEntity activityAccountEntity = new ActivityAccountEntity();
        BeanUtils.copyProperties(dbActivityAccount, activityAccountEntity);
        return activityAccountEntity;
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
        ActivityAccountMonth activityAccountMonth = new ActivityAccountMonth();
        activityAccountMonth.setUserId(userId);
        activityAccountMonth.setActivityId(activityId);
        activityAccountMonth.setMonth(month);
        ActivityAccountMonth dbActivityAccountMonth = activityAccountMonthMapper.queryActivityAccountMonthByUserId(activityAccountMonth);
        if (dbActivityAccountMonth == null){
            return null;
        }
        ActivityAccountMonthEntity activityAccountMonthEntity = new ActivityAccountMonthEntity();
        BeanUtils.copyProperties(dbActivityAccountMonth, activityAccountMonthEntity);
        return activityAccountMonthEntity;
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        ActivityAccountDay activityAccountDay = new ActivityAccountDay();
        activityAccountDay.setUserId(userId);
        activityAccountDay.setActivityId(activityId);
        activityAccountDay.setDay(day);
        ActivityAccountDay dbActivityAccountDay = activityAccountDayMapper.queryActivityAccountDayByUserId(activityAccountDay);
        if (dbActivityAccountDay == null){
            return null;
        }
        ActivityAccountDayEntity activityAccountDayEntity = new ActivityAccountDayEntity();
        BeanUtils.copyProperties(dbActivityAccountDay, activityAccountDayEntity);
        return activityAccountDayEntity;
    }

    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        try {
            String userId = createPartakeOrderAggregate.getUserId();
            Long activityId = createPartakeOrderAggregate.getActivityId();
            ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
            ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
            ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
            UserOrderResEntity userOrderResEntity = createPartakeOrderAggregate.getUserOrderResEntity();

            // 统一切换路由，以下事务内的所有操作，都走一个路由
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 1. 更新总账户 activity_account
                    LambdaUpdateWrapper<ActivityAccount> activityAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<ActivityAccount>()
                            .setSql("total_count_surplus = total_count_surplus - 1")
                            .setSql("month_count_surplus = month_count_surplus - 1")
                            .setSql("day_count_surplus = day_count_surplus - 1")
                            .set(ActivityAccount::getUpdateTime, new Date())
                            .eq(ActivityAccount::getUserId, userId)
                            .eq(ActivityAccount::getActivityId, activityId)
                            .gt(ActivityAccount::getTotalCountSurplus, 0)
                            .gt(ActivityAccount::getMonthCountSurplus, 0)
                            .gt(ActivityAccount::getDayCountSurplus, 0);
                    int totalCount = activityAccountMapper.update(null, activityAccountLambdaUpdateWrapper);
                    if (totalCount!=1) {
                        status.setRollbackOnly();
                        log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
                        throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getMessage());
                    }

                    // 2. 创建/更新月账户 activity_account_month
                    if (createPartakeOrderAggregate.isExistAccountMonth()) {
                        //更新
                        LambdaUpdateWrapper<ActivityAccountMonth> activityAccountMonthLambdaUpdateWrapper = new LambdaUpdateWrapper<ActivityAccountMonth>()
                                .setSql("month_count_surplus = month_count_surplus - 1")
                                .set(ActivityAccountMonth::getUpdateTime, new Date())
                                .eq(ActivityAccountMonth::getUserId, userId)
                                .eq(ActivityAccountMonth::getActivityId, activityId)
                                .eq(ActivityAccountMonth::getMonth, activityAccountMonthEntity.getMonth())
                                .gt(ActivityAccountMonth::getMonthCountSurplus, 0);
                        int updateMonthCount = activityAccountMonthMapper.update(null, activityAccountMonthLambdaUpdateWrapper);
                        if (updateMonthCount!=1) {
                            // 未更新成功则回滚
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {} month: {}", userId, activityId, activityAccountMonthEntity.getMonth());
                            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getMessage());
                        }
                    } else {
                        //创建
                        ActivityAccountMonth activityAccountMonth = new ActivityAccountMonth();
                        BeanUtils.copyProperties(activityAccountMonthEntity, activityAccountMonth);
                        activityAccountMonth.setMonthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus()-1);
                        activityAccountMonthMapper.insert(activityAccountMonth);
                        // 新创建月账户，则更新总账表中月镜像额度
                        LambdaUpdateWrapper<ActivityAccount> activityAccountLambdaUpdateWrapperM = new LambdaUpdateWrapper<ActivityAccount>()
                                .set(ActivityAccount::getMonthCountSurplus, activityAccountEntity.getMonthCountSurplus()-1)
                                .set(ActivityAccount::getUpdateTime, new Date())
                                .eq(ActivityAccount::getUserId, userId)
                                .eq(ActivityAccount::getActivityId, activityId)
                                .gt(ActivityAccount::getMonthCountSurplus, 0);
                        activityAccountMapper.update(null, activityAccountLambdaUpdateWrapperM);
                    }

                    // 3. 创建/更新日账户 activity_account_day
                    if (createPartakeOrderAggregate.isExistAccountDay()) {
                        //更新
                        LambdaUpdateWrapper<ActivityAccountDay> activityAccountDayLambdaUpdateWrapper = new LambdaUpdateWrapper<ActivityAccountDay>()
                                .setSql("day_count_surplus = day_count_surplus - 1") //因为创建每有原数据，所以创建时不能用这种形式
                                .set(ActivityAccountDay::getUpdateTime, new Date())
                                .eq(ActivityAccountDay::getUserId, userId)
                                .eq(ActivityAccountDay::getActivityId, activityId)
                                .eq(ActivityAccountDay::getDay, activityAccountDayEntity.getDay())
                                .gt(ActivityAccountDay::getDayCountSurplus, 0);
                        int updateDayCount = activityAccountDayMapper.update(null, activityAccountDayLambdaUpdateWrapper);
                        if (updateDayCount!=1) {
                            // 未更新成功则回滚
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {} day: {}", userId, activityId, activityAccountDayEntity.getDay());
                            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getMessage());
                        }
                    } else {
                        //创建
                        ActivityAccountDay activityAccountDay = new ActivityAccountDay();
                        BeanUtils.copyProperties(activityAccountDayEntity, activityAccountDay);
                        //日总额度为总账户的，但日剩余额度要-1，因为创建时你已经花了一次了
                        activityAccountDay.setDayCountSurplus(activityAccountDayEntity.getDayCountSurplus()-1);
                        activityAccountDayMapper.insert(activityAccountDay);
                        // 新创建日账户，则更新总账表中日镜像额度
                        LambdaUpdateWrapper<ActivityAccount> activityAccountLambdaUpdateWrapperD = new LambdaUpdateWrapper<ActivityAccount>()
                                .set(ActivityAccount::getDayCountSurplus, activityAccountEntity.getDayCountSurplus()-1)
                                .set(ActivityAccount::getUpdateTime, new Date())
                                .eq(ActivityAccount::getUserId, userId)
                                .eq(ActivityAccount::getActivityId, activityId)
                                .gt(ActivityAccount::getDayCountSurplus, 0);
                        activityAccountMapper.update(null, activityAccountLambdaUpdateWrapperD);
                    }

                    // 4. 写入参与活动订单 user_order
                    UserOrder userOrder = new UserOrder();
                    BeanUtils.copyProperties(userOrderResEntity, userOrder);
                    userOrder.setOrderState(userOrderResEntity.getOrderState().getCode());
                    userOrderMapper.insert(userOrder);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入创建参与活动记录，唯一索引冲突 userId: {} activityId: {}", userId, activityId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

    }


}
