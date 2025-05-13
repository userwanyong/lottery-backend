package com.lottery.test.trigger;


import com.alibaba.fastjson.JSON;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.trigger.api.dto.req.ActivityDrawRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityDrawResponseDTO;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DubboTest {

    @DubboReference(interfaceClass = LotteryActivityService.class, version = "1.0")
    private LotteryActivityService raffleActivityService;

    @Test
    public void test_rpc() {
        ActivityDrawRequestDTO request = new ActivityDrawRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("yong");
        BaseResponse<ActivityDrawResponseDTO> response = raffleActivityService.draw(request);

        log.info("请求参数：{}", JSON.toJSONString(request));
        log.info("测试结果：{}", JSON.toJSONString(response));
    }

}