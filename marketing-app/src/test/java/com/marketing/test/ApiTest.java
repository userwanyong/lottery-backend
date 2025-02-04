package com.marketing.test;

import com.alibaba.fastjson.JSON;
import com.marketing.infrastructure.persistent.dao.AwardMapper;
import com.marketing.infrastructure.persistent.po.Award;
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
public class ApiTest {

    @Resource
    private AwardMapper awardMapper;
    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardMapper.queryAwardList();
        log.info("测试结果：{}", JSON.toJSONString(awards));
    }

}
