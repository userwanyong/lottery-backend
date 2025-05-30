package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【activity(抽奖活动表)】的数据库操作Mapper
*/
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

}




