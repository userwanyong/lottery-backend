package com.lottery.test.domain.activity;

import com.lottery.domain.activity.model.entity.QuotaOrderEntity;
import com.lottery.domain.activity.model.valobj.OrderTradeTypeVO;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.activity.service.armory.ActivityArmory;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityQuotaServiceTest {
    @Resource
    private ActivityQuotaService activityQuotaService;
    @Resource
    private ActivityArmory activityArmory;

    @Test
    public void test_armory(){
        boolean b = activityArmory.assembleActivitySku(9011L);
        if (b){
            log.info("活动装配成功");
        }
    }

    /**
     * 增加账户额度
     */
    @Test
    public void test_createQuotaOrder() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            try {
                QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
                quotaOrderEntity.setUserId("yong");
                quotaOrderEntity.setSku(9011L);
                // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
                quotaOrderEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
                quotaOrderEntity.setOrderTradeTypeVO(OrderTradeTypeVO.rebate_no_pay_trade);
                String orderId = activityQuotaService.createQuotaOrder(quotaOrderEntity);
                log.info("测试结果：{}", orderId);
            } catch (AppException e) {
                log.warn(e.getMessage());
            }
        }
//        new CountDownLatch(1).await();
    }

    @Test
    public void test_credit_pay_trade() {
        QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
        quotaOrderEntity.setUserId("yong");
        quotaOrderEntity.setSku(9011L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        quotaOrderEntity.setOutBusinessNo("70009240609001");
        quotaOrderEntity.setOrderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade);
        String orderId = activityQuotaService.createQuotaOrder(quotaOrderEntity);
        log.info("测试结果：{}", orderId);
    }
}
