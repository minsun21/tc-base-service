package com.demo.tcservicebase.common.config;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * ERP DB 전용 데이터소스/MyBatis 설정.
 * 이 데이터소스를 쓰는 Mapper는 domain.erp 패키지 아래에 두고,
 * 매퍼 XML은 resources/mapper/erp/ 아래에 둔다.
 */
@Configuration
@MapperScan(basePackages = "com.demo.tcservicebase.domain.erp", sqlSessionFactoryRef = "erpSqlSessionFactory")
public class ErpDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.erp")
    public DataSourceProperties erpDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource erpDataSource(DataSourceProperties erpDataSourceProperties) {
        return erpDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public SqlSessionFactory erpSqlSessionFactory(DataSource erpDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(erpDataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/erp/**/*.xml"));

        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        mybatisConfig.setDefaultFetchSize(500);
        mybatisConfig.setLogImpl(Slf4jImpl.class);
        factoryBean.setConfiguration(mybatisConfig);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate erpSqlSessionTemplate(SqlSessionFactory erpSqlSessionFactory) {
        return new SqlSessionTemplate(erpSqlSessionFactory);
    }
}