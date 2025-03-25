package com.lottery.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.credit.model.aggregate.TradeAggregate;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.CreditOrderEntity;
import com.lottery.domain.credit.repository.CreditRepository;
import com.lottery.infrastructure.persistent.dao.CreditAccountMapper;
import com.lottery.infrastructure.persistent.dao.UserCreditOrderMapper;
import com.lottery.infrastructure.persistent.po.CreditAccount;
import com.lottery.infrastructure.persistent.po.UserCreditOrder;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
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
    private UserCreditOrderMapper userCreditOrderMapper;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public void saveTradeAggregate(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();

        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setUserId(userId);
        creditAccount.setAccountStatus("open");// TODO 暂时设为open，后期更改
        creditAccount.setTotalAmount(creditAccountEntity.getCreditAmount());
        creditAccount.setAvailableAmount(creditAccountEntity.getCreditAmount());

        UserCreditOrder userCreditOrder = new UserCreditOrder();
        userCreditOrder.setUserId(userId);
        userCreditOrder.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrder.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrder.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrder.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrder.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        RLock lock = redisService.getLock(Constants.RedisKey.CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());

        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 保存账户
                    LambdaQueryWrapper<CreditAccount> queryWrapper = new QueryWrapper<CreditAccount>().lambda()
                            .eq(CreditAccount::getUserId, userId);
                    CreditAccount account = creditAccountMapper.selectOne(queryWrapper);
                    if (account == null){
                        // 新增
                        creditAccountMapper.insert(creditAccount);
                    }else {
                        // 更新
                        creditAccountMapper.update(creditAccount);
                    }
                    // 保存订单
                    userCreditOrderMapper.insert(userCreditOrder);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }
    }
}
