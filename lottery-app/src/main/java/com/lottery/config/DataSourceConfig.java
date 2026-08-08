package com.lottery.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author 永
 * 数据源配置（轻量版：单 MySQL 数据源，HikariCP）
 * Bean 名沿用 mysqlDataSource 以兼容历史注入点（如 BenchmarkController）。
 */
@Configuration
public class DataSourceConfig {

    /**
     * 主数据源：由 spring.datasource + spring.datasource.hikari 配置。
     * 使用 DataSourceProperties.initializeDataSourceBuilder 正确处理 url→jdbcUrl 映射，
     * 池参数通过 @ConfigurationProperties("spring.datasource.hikari") 绑定。
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource mysqlDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Configuration
    @MapperScan(basePackages = "com.lottery.infrastructure.dao", sqlSessionFactoryRef = "mysqlSqlSessionFactory")
    static class MysqlMyBatisPlusConfig {

        @Bean("mysqlSqlSessionFactory")
        public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
            // 开启 mybatis-plus 分页功能
            MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
            mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // MySQL 分页支持

            MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setPlugins(mybatisPlusInterceptor);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath:mapper/mysql/*.xml"));

            // 使用 GlobalConfig 配置 MyBatis-Plus 特性
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setBanner(false); // 关闭启动 banner
            globalConfig.setDbConfig(new GlobalConfig.DbConfig().setTableUnderline(true)); // 下划线转驼峰
            factoryBean.setGlobalConfig(globalConfig);

            return factoryBean.getObject();
        }
    }

}
