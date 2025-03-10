package com.lottery.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.activity.model.entity.PartakeOrderReqEntity;
import com.lottery.domain.activity.model.entity.PartakeOrderResEntity;
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
        PartakeOrderReqEntity partakeOrderReqEntity = new PartakeOrderReqEntity();
        partakeOrderReqEntity.setUserId("yong");
        partakeOrderReqEntity.setActivityId(100301L);
        // 调用接口
        PartakeOrderResEntity res = activityPartakeService.createPartakeOrder(partakeOrderReqEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeOrderReqEntity));
        log.info("测试结果：{}", JSON.toJSONString(res));
    }
}
