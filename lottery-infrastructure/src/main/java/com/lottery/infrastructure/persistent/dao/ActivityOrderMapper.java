package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.persistent.po.ActivityOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【activity_order(抽奖活动单)】的数据库操作Mapper
 */
@Mapper
@DBRouterStrategy(splitTable = true) //执行 MyBaits 操作的时候，对 SQL 语句进行动态变更。
public interface ActivityOrderMapper extends BaseMapper<ActivityOrder> {


    void add(ActivityOrder activityOrder);
}




