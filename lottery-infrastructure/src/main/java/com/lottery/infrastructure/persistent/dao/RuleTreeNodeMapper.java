package com.lottery.infrastructure.persistent.dao;

import com.lottery.infrastructure.persistent.po.RuleTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【rule_tree_node】的数据库操作Mapper
*/
@Mapper
public interface RuleTreeNodeMapper extends BaseMapper<RuleTreeNode> {

}




