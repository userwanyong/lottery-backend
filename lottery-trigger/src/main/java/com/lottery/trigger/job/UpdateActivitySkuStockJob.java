package com.lottery.trigger.job;

import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.service.ActivitySkuStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 定时任务-更新数据库suk库存
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {
    @Resource
    private ActivitySkuStockService skuStock;
    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (activitySkuStockKeyVO==null) {
                log.info("【定时任务】-暂无更新活动sku库存任务");
                return;
            }
            log.info("【定时任务】-更新活动sku库存-成功 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("【定时任务】-更新活动sku库存-失败", e);
        }
    }
}
