package com.lottery.infrastructure.persistent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【strategy(策略表)】的数据库操作Mapper
 */
@Mapper
public interface StrategyMapper extends BaseMapper<Strategy> {

}




