package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lottery.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.lottery.domain.activity.model.aggregate.CreateOrderAggregate;
import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.model.valobj.ActivityStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
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
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        try {
            // 订单对象
            ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
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
            activityOrder.setTotalCount(createOrderAggregate.getTotalCount());
            activityOrder.setDayCount(createOrderAggregate.getDayCount());
            activityOrder.setMonthCount(createOrderAggregate.getMonthCount());
            activityOrder.setState(activityOrderEntity.getState().getCode());
            activityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            // 账户对象
            ActivityAccount activityAccount = new ActivityAccount();
            BeanUtils.copyProperties(activityOrderEntity, activityAccount);
            activityAccount.setTotalCountSurplus(activityOrderEntity.getTotalCount());
            activityAccount.setDayCountSurplus(activityOrderEntity.getDayCount());
            activityAccount.setMonthCountSurplus(activityOrderEntity.getMonthCount());

            // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(createOrderAggregate.getUserId());
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
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
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

}
