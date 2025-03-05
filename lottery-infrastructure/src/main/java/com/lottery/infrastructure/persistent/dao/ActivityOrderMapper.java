package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.lottery.infrastructure.persistent.po.ActivityOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 永
* @description 针对表【activity_order(抽奖活动单)】的数据库操作Mapper
*/
@Mapper
@DBRouterStrategy(splitTable = true) //执行 MyBaits 操作的时候，对 SQL 语句进行动态变更。
public interface ActivityOrderMapper extends BaseMapper<ActivityOrder> {

    @DBRouter(key = "userId") //指定对哪个SQL的操作进行路由,默认路由字段就是 userId
    void myinsert(ActivityOrder activityOrder);

    @DBRouter
    List<ActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}




