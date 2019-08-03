package com.daishun.springmybatissimpleexample.dao.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author daishun
 * @since 2019/7/31
 */
@Configuration
@MapperScan(basePackages = {"com.daishun.springmybatissimpleexample.dao.dao"}, sqlSessionFactoryRef = "exampleSqlSessionFactory")
@tk.mybatis.spring.annotation.MapperScan(basePackages = {"com.daishun.springmybatissimpleexample.dao.mapper"})
public class DataSourceConfig {

    @Bean(name = "exampleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.example")
    @Primary
    public DataSource exampleDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "exampleSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(exampleDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] xmlResources = resolver.getResources("classpath:mybatis/*/*.xml");
        sessionFactory.setMapperLocations(xmlResources);
        return sessionFactory.getObject();
    }

    @Bean(name = "exampleTransactionManager")
    @Primary
    public DataSourceTransactionManager bizTransactionManager(@Qualifier("exampleDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
