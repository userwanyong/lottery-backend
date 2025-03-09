package com.lottery.infrastructure.persistent.dao;

import com.lottery.infrastructure.persistent.po.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【task(任务表，发送MQ)】的数据库操作Mapper
*/
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}




