package com.lottery.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.lottery.infrastructure.persistent.dao.AwardMapper;
import com.lottery.infrastructure.persistent.po.Award;
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
public class AwardMapperTest {

    @Resource
    private AwardMapper awardMapper;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardMapper.queryAwardList();
        log.info("测试结果：{}", JSON.toJSONString(awards));
    }

}
