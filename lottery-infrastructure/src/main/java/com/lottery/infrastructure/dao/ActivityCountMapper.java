package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.ActivityCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【activity_count(抽奖活动次数配置表)】的数据库操作Mapper
*/
@Mapper
public interface ActivityCountMapper extends BaseMapper<ActivityCount> {

}




