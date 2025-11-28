package com.lottery.test.domain.award;

import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.service.UserAwardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAwardServiceTest {
    @Resource
    private UserAwardService userAwardService;

    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("yong");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
//            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101L);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardState(AwardStateVO.create);
            userAwardService.saveUserAwardRecord(userAwardRecordEntity);
            Thread.sleep(500);
        }
        new CountDownLatch(1).await();
    }

}
