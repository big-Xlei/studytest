package com.xl.study.studytest.moredbconf;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement //开启事务
@Configuration  //spring中常用到注解，与xml配置相对立。是两种加载bean方式
@MapperScan("com.maicedata.yuhang.population.dao.*") // 扫描mapperdao的地址
public class MybatisPlusMoreDbConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        paginationInterceptor.setLocalPage(true); // 由于版本问题，有些类可能招不到这个方法，需要升级jar包
        return paginationInterceptor;
    }

    @Bean(name = "pgsql")
    @ConfigurationProperties(prefix = "spring.datasource.druid.pgsql")
    public DataSource pgsql() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "spring.datasource.druid.mysql")
    public DataSource mysql() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("pgsql") DataSource pgsql,
                                         @Qualifier("mysql") DataSource mysql) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.pgsql.getValue(), pgsql);
        targetDataSources.put(DBTypeEnum.mysql.getValue(), mysql);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(mysql); // 程序默认数据源，这个要根据程序调用数据源频次，经常把常调用的数据源作为默认
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(pgsql(), mysql()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //PerformanceInterceptor(),OptimisticLockerInterceptor()
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
//        sqlSessionFactory.setGlobalConfig(globalConfiguration()); //注释掉全局配置，因为在xml中读取就是全局配置
        return sqlSessionFactory.getObject();
    }

 /*   @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(0);
        conf.setMetaObjectHandler(new MyMetaObjectHandler());
        conf.setDbColumnUnderline(true);
        conf.setRefresh(true);
        return conf;
    }*/
}
