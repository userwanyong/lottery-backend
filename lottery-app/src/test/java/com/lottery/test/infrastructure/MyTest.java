package com.lottery.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.lottery.infrastructure.persistent.dao.ActivityOrderMapper;
import com.lottery.infrastructure.persistent.po.ActivityOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Resource
    private ActivityOrderMapper activityOrderMapper;

    @Test
    public void test_insert() {
        ActivityOrder activityOrder = new ActivityOrder();
        activityOrder.setUserId("219621515656");
        activityOrder.setActivityId(100301L);
        activityOrder.setActivityName("测试活动");
        activityOrder.setStrategyId(100006L);
        activityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrder.setOrderTime(new Date());
        activityOrder.setState("complete");
        activityOrder.setSku(100001L);
        activityOrder.setDayCount(100);
        activityOrder.setMonthCount(1000);
        activityOrder.setTotalCount(10000);
        // 插入数据
        activityOrderMapper.myinsert(activityOrder);
    }

    @Test
    public void test_queryActivityOrderByUserId() {
        String userId = "219621515656";
        List<ActivityOrder> activityOrder = activityOrderMapper.queryRaffleActivityOrderByUserId(userId);
        log.info("测试结果：{}", JSON.toJSONString(activityOrder));
    }
}
