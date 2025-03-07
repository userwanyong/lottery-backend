package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.activity.model.aggregate.CreateOrderAggregate;
import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.infrastructure.persistent.dao.*;
import com.lottery.infrastructure.persistent.po.*;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
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

}
