package com.lottery.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.credit.model.aggregate.TradeAggregate;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.CreditOrderEntity;
import com.lottery.domain.credit.model.entity.TaskEntity;
import com.lottery.domain.credit.model.valobj.CreditAccountStatusVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import com.lottery.domain.credit.repository.CreditRepository;
import com.lottery.infrastructure.dao.po.CreditRecord;
import com.lottery.infrastructure.dao.CreditAccountMapper;
import com.lottery.infrastructure.dao.TaskMapper;
import com.lottery.infrastructure.dao.CreditRecordMapper;
import com.lottery.infrastructure.dao.po.CreditAccount;
import com.lottery.infrastructure.dao.po.Task;
import com.lottery.infrastructure.event.LocalMessageEvent;
import com.lottery.infrastructure.redis.RedisService;
import org.springframework.context.ApplicationEventPublisher;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author 永
 * 积分领域仓储实现
 */
@Repository
@Slf4j
public class CreditRepositoryImpl implements CreditRepository {
    @Resource
    private RedisService redisService;
    @Resource
    private CreditAccountMapper creditAccountMapper;
    @Resource
    private CreditRecordMapper creditRecordMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void saveTradeAggregate(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        CreditAccount creditAccount = new CreditAccount();
        BigDecimal creditAmount = creditAccountEntity.getCreditAmount();
        creditAccount.setUserId(userId);
        creditAccount.setActivityId(creditAccountEntity.getActivityId());
        creditAccount.setAccountStatus(CreditAccountStatusVO.OPEN.getCode());
        creditAccount.setTotalAmount(creditAmount);
        creditAccount.setAvailableAmount(creditAccountEntity.getCreditAmount());

        CreditRecord creditRecord = new CreditRecord();
        creditRecord.setUserId(userId);
        creditRecord.setActivityId(creditOrderEntity.getActivityId());
        creditRecord.setTradeName(creditOrderEntity.getTradeName().getName());
        creditRecord.setTradeType(creditOrderEntity.getTradeType().getCode());
        creditRecord.setTradeAmount(creditOrderEntity.getTradeAmount());
        creditRecord.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setActivityId(taskEntity.getActivityId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        RLock lock = redisService.getLock(Constants.RedisKey.CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());

        try {
            lock.lock(3, TimeUnit.SECONDS);
            transactionTemplate.execute(status -> {
                try {
                    // 保存账户
                    LambdaQueryWrapper<CreditAccount> queryWrapper = new QueryWrapper<CreditAccount>().lambda()
                            .eq(CreditAccount::getUserId, userId)
                            .eq(CreditAccount::getActivityId, creditOrderEntity.getActivityId());
                    CreditAccount account = creditAccountMapper.selectOne(queryWrapper);
                    if (account == null) {
                        // 新增
                        creditAccountMapper.insert(creditAccount);
                        log.debug("[CreditRepositoryImpl]创建积分账户成功 userId:{}", userId);
                    } else if (creditOrderEntity.getTradeType() == TradeTypeVO.FORWARD) {
                        // 增加(可用)
                        creditAccountMapper.update(creditAccount);
                        log.debug("[CreditRepositoryImpl]增加积分账户成功 userId:{}", userId);
                    } else {
                        // 减少(可用)
                        creditAccountMapper.reduce(creditAccount);
                        log.debug("[CreditRepositoryImpl]减少积分账户成功 userId:{}", userId);
                    }
                    // 保存订单
                    creditRecordMapper.insert(creditRecord);
                    log.debug("[CreditRepositoryImpl]保存积分订单成功 userId:{}", userId);
                    // 写入任务
                    taskMapper.insert(task);
                    log.debug("[CreditRepositoryImpl]写入积分任务成功 userId:{}", userId);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.warn("[CreditRepositoryImpl]调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId());
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("[CreditRepositoryImpl]调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
            });
        } finally {
            lock.unlock();
        }

        // task 已在事务内写入（state=create）。事务提交后立即异步分发（轻量版：Spring event 替代 MQ，准实时），
        // 失败/崩溃由 SendMessageTaskJob 扫表补偿，保证最终一致性。
        applicationEventPublisher.publishEvent(new LocalMessageEvent(this,
                taskEntity.getTopic(), JSON.toJSONString(taskEntity.getMessage()),
                taskEntity.getUserId(), taskEntity.getMessageId()));
    }

    @Override
    public CreditAccountEntity queryUserCreditAccount(String userId,Long activityId) {
        LambdaQueryWrapper<CreditAccount> queryWrapper = new QueryWrapper<CreditAccount>().lambda()
                .eq(CreditAccount::getUserId, userId)
                .eq(CreditAccount::getActivityId, activityId);
        CreditAccount creditAccount = creditAccountMapper.selectOne(queryWrapper);
        if (creditAccount == null) {
            return CreditAccountEntity.builder()
                    .userId(userId)
                    .creditAmount(BigDecimal.ZERO)
                    .build();
        }
        return CreditAccountEntity.builder()
                .userId(creditAccount.getUserId())
                .creditAmount(creditAccount.getAvailableAmount())
                .build();

    }
}
