package com.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author 永
 * @description 针对表【strategy_award(策略_奖品详情表)】的数据库操作Mapper
 */
@Mapper
public interface StrategyAwardMapper extends BaseMapper<StrategyAward> {

}




