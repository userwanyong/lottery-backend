package com.lottery.config;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 永
 * 雪花id生成器配置
 */
@Configuration
public class BusinessIdGeneratorConfig {
    @Bean
    public void idGeneratorOptions() {
        IdGeneratorOptions options = new IdGeneratorOptions((short) 16);
        YitIdHelper.setIdGenerator(options);
    }

}
