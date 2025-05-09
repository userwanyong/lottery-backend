package com.lottery.trigger.job;

import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.service.ActivitySkuStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 永
 * 定时任务-更新数据库suk库存
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {
    @Resource
    private ActivitySkuStockService skuStock;
    @Resource
    private ThreadPoolExecutor executor;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            List<Long> skuList = skuStock.querySkuList();
            for (Long sku : skuList) {
                executor.execute(() -> {
                    ActivitySkuStockKeyVO activitySkuStockKeyVO = null;
                    try{
                        activitySkuStockKeyVO = skuStock.takeQueueValue(sku);
                    }catch (Exception e){
                        log.error("【定时任务-UpdateActivitySkuStockJob】-更新活动sku库存-失败", e);
                    }

                    if (activitySkuStockKeyVO==null) {
                        return;
                    }
                    skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
                    log.info("【定时任务-UpdateActivitySkuStockJob】-更新活动sku库存-成功 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
                });
            }

        } catch (Exception e) {
            log.error("【定时任务-UpdateActivitySkuStockJob】-更新活动sku库存-失败", e);
        }
    }
}
