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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 팀센터 DB 전용 데이터소스/MyBatis 설정.
 * 이 데이터소스를 쓰는 Mapper는 domain.tc 패키지 아래에 두고,
 * 매퍼 XML은 resources/mapper/tc/ 아래에 둔다.
 */
@Configuration
@MapperScan(basePackages = "com.demo.tcservicebase.domain.tc", sqlSessionFactoryRef = "tcSqlSessionFactory")
public class TcDataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.tc")
    public DataSourceProperties tcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource tcDataSource(DataSourceProperties tcDataSourceProperties) {
        return tcDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    public SqlSessionFactory tcSqlSessionFactory(DataSource tcDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(tcDataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/tc/**/*.xml"));
        factoryBean.setTypeAliasesPackage("com.demo.tcservicebase.domain.tc.dto");

        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        mybatisConfig.setDefaultFetchSize(500);
        mybatisConfig.setLogImpl(Slf4jImpl.class);
        factoryBean.setConfiguration(mybatisConfig);

        return factoryBean.getObject();
    }

    @Primary
    @Bean
    public SqlSessionTemplate tcSqlSessionTemplate(SqlSessionFactory tcSqlSessionFactory) {
        return new SqlSessionTemplate(tcSqlSessionFactory);
    }
}