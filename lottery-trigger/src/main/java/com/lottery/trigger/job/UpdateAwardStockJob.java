package com.lottery.trigger.job;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.service.lottery.Lottery;
import com.lottery.domain.strategy.service.lottery.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 定时任务-更新数据库奖品库存
 */
@Slf4j
@Component
public class UpdateAwardStockJob {
    @Resource
    private Stock stock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            LotteryReqEntity lotteryReqEntity = stock.takeQueueValue();
            if (lotteryReqEntity == null) {
                log.info("【定时任务】-暂无更新数据库奖品库存任务");
                return;
            }
            stock.updateStrategyAwardStock(lotteryReqEntity.getStrategyId(), lotteryReqEntity.getAwardId());
            log.info("【定时任务】-更新数据库奖品库存-成功 strategyId:{} awardId:{}", lotteryReqEntity.getStrategyId(), lotteryReqEntity.getAwardId());
        } catch (Exception e) {
            log.error("【定时任务】-更新数据库奖品库存-失败", e);
        }
    }
}
