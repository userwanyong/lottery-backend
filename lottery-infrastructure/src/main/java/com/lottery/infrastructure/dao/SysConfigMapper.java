package com.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.SysConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统动态配置 Mapper（轻量版：替代 Zookeeper DCC）
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 插入或更新（按 config_key 主键 upsert）
     */
    @Insert("insert into sys_config(config_key, config_value, remark, update_time) " +
            "values(#{configKey}, #{configValue}, #{remark}, now()) " +
            "on duplicate key update config_value = #{configValue}, update_time = now()")
    void upsert(SysConfig config);
}
