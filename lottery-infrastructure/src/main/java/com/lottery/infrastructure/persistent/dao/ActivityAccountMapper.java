package com.lottery.infrastructure.persistent.dao;

import com.lottery.infrastructure.persistent.po.ActivityAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【activity_account(抽奖活动账户表)】的数据库操作Mapper
*/
@Mapper
public interface ActivityAccountMapper extends BaseMapper<ActivityAccount> {

    int updateAccount(ActivityAccount activityAccount);

    void add(ActivityAccount activityAccount);
}




