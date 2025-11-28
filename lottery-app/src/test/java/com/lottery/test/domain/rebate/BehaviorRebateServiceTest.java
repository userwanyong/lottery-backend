package com.lottery.test.domain.rebate;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.service.RebateService;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BehaviorRebateServiceTest {
    @Resource
    private RebateService rebateService;
    @Resource
    private LotteryActivityService lotteryActivityService;

    @Test
    public void test_createRebateOrder() {
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId("yong");
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
        // 重复的 OutBusinessNo 会报错唯一索引冲突，这也是保证幂等的手段，确保不会多记账
        behaviorEntity.setOutBusinessNo("20240430");
        List<String> orderIds = rebateService.createRebateOrder(behaviorEntity);
        log.info("测试结果：{}", JSON.toJSONString(orderIds));
    }

    @Test
    public void test_calendarSignRebate() {
//        BaseResponse<Boolean> response = lotteryActivityService.calendarSignRebate("yong");
//        log.info("测试结果：{}", JSON.toJSONString(response));
    }
}
