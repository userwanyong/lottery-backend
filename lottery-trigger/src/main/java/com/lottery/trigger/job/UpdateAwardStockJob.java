package com.lottery.trigger.job;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.service.Stock;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 永
 * 定时任务-更新数据库奖品库存
 */
@Slf4j
@Component
public class UpdateAwardStockJob {
    @Resource
    private Stock stock;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private RedissonClient redissonClient;


    @XxlJob("updateAwardStockJob")
    public void exec() {
        // 为什么加锁？分布式应用N台机器部署互备，任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("lottery-updateAwardStockJob");
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) {
                return;
            }

            List<String> awardList = stock.getStrategyAwardList();
            for (String strategyAward : awardList) {
                executor.execute(() -> {
                    LotteryReqEntity lotteryReqEntity = null;
                    try {
                        lotteryReqEntity = stock.takeQueueValue(strategyAward);
                    } catch (Exception e) {
                        log.error("【定时任务】-更新数据库奖品库存-失败", e);
                    }

                    if (lotteryReqEntity == null) {
//                        log.info("【定时任务】-暂无更新数据库奖品库存任务");
                        return;
                    }
                    stock.updateStrategyAwardStock(lotteryReqEntity.getStrategyId(), lotteryReqEntity.getAwardId());
                    log.info("【定时任务】-更新数据库奖品库存-成功 strategyId:{} awardId:{}", lotteryReqEntity.getStrategyId(), lotteryReqEntity.getAwardId());
                });
            }
        } catch (Exception e) {
            log.error("【定时任务】-更新数据库奖品库存-失败", e);
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }
}
