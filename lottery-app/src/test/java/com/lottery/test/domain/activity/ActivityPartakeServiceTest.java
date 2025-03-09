package com.lottery.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.activity.model.entity.UserOrderReqEntity;
import com.lottery.domain.activity.model.entity.UserOrderResEntity;
import com.lottery.domain.activity.service.ActivityPartakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityPartakeServiceTest {
    @Resource
    private ActivityPartakeService activityPartakeService;

    @Test
    public void test_createPartakeOrder() {
        // 请求参数
        UserOrderReqEntity userOrderReqEntity = new UserOrderReqEntity();
        userOrderReqEntity.setUserId("yong");
        userOrderReqEntity.setActivityId(100301L);
        // 调用接口
        UserOrderResEntity res = activityPartakeService.createPartakeOrder(userOrderReqEntity);
        log.info("请求参数：{}", JSON.toJSONString(userOrderReqEntity));
        log.info("测试结果：{}", JSON.toJSONString(res));
    }
}
