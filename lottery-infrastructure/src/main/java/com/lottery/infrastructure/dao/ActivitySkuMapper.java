package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.ActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【activity_sku(抽奖活动sku表)】的数据库操作Mapper
*/
@Mapper
public interface ActivitySkuMapper extends BaseMapper<ActivitySku> {

}




