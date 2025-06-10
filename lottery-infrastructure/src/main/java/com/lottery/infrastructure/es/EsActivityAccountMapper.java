package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.ActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【activity_account(抽奖活动账户表)】的数据库操作Mapper
 */
@Mapper
public interface EsActivityAccountMapper extends BaseMapper<ActivityAccount> {

}




