package com.lottery.infrastructure.persistent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.persistent.po.Rule;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【rule(规则表)】的数据库操作Mapper
 */
@Mapper
public interface RuleMapper extends BaseMapper<Rule> {

}




