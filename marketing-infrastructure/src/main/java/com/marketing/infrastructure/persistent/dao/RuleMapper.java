package com.marketing.infrastructure.persistent.dao;

import com.marketing.infrastructure.persistent.po.Rule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【rule(规则表)】的数据库操作Mapper
*/
@Mapper
public interface RuleMapper extends BaseMapper<Rule> {

}




