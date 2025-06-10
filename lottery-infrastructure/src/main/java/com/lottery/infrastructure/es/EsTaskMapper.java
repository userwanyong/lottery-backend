package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【task(任务表，发送MQ)】的数据库操作Mapper
 */
@Mapper
public interface EsTaskMapper extends BaseMapper<Task> {

}




