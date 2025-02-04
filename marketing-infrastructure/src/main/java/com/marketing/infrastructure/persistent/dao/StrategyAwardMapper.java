package com.marketing.infrastructure.persistent.dao;

import com.marketing.infrastructure.persistent.po.StrategyAward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【strategy_award(策略_奖品详情表)】的数据库操作Mapper
*/
@Mapper
public interface StrategyAwardMapper extends BaseMapper<StrategyAward> {

}




