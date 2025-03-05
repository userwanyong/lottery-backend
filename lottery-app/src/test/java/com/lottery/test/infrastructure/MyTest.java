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
        ActivityOrder raffleActivityOrder = new ActivityOrder();
        raffleActivityOrder.setUserId("219621515656");
        raffleActivityOrder.setActivityId(100301L);
        raffleActivityOrder.setActivityName("测试活动");
        raffleActivityOrder.setStrategyId(100006L);
        raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setState(0);
        // 插入数据
        activityOrderMapper.myinsert(raffleActivityOrder);
    }

    @Test
    public void test_queryRaffleActivityOrderByUserId() {
        String userId = "219621515656";
        List<ActivityOrder> raffleActivityOrders = activityOrderMapper.queryRaffleActivityOrderByUserId(userId);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrders));
    }
}
