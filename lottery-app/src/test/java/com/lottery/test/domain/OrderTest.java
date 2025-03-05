package com.lottery.test.domain;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import com.lottery.domain.activity.model.entity.ActivityShopCartEntity;
import com.lottery.domain.activity.service.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {
    @Resource
    private Order order;

    @Test
    public void test_createActivityOrder() {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("yong");
        activityShopCartEntity.setSku(10001L);
        ActivityOrderEntity raffleActivityOrder = order.createActivityOrder(activityShopCartEntity);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrder));
    }
}
