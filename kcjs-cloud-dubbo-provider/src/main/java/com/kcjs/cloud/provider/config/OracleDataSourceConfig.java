package com.kcjs.cloud.provider.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.kcjs.cloud.oracle.mapper", sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class OracleDataSourceConfig {

    @Primary
    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle.hikari")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "oracleSqlSessionFactory")
    public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();//MyBatis-Plus 必须用
        factoryBean.setDataSource(oracleDataSource);
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public DataSourceTransactionManager oracleTransactionManager(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
        return new DataSourceTransactionManager(oracleDataSource);
    }
}