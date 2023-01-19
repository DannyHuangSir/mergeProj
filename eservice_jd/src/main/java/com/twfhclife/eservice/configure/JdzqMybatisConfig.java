package com.twfhclife.eservice.configure;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

import static com.twfhclife.eservice.configure.JdzqMybatisConfig.PACKAGE;

@Configuration
@MapperScan(basePackages = PACKAGE, sqlSessionFactoryRef = "jdzqSqlSessionFactory")
public class JdzqMybatisConfig {

    static final String PACKAGE = "com.twfhclife.eservice.jdzq.dao";
    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/jdzq/*.xml";

    @Bean(name = "jdzqDataSource")
    @ConfigurationProperties(prefix = "jdzq.datasource")
    public DataSource jdzqDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdzqTransactionManager")
    public DataSourceTransactionManager jdzqTransactionManager() {
        return new DataSourceTransactionManager(jdzqDataSource());
    }

    @Bean(name = "jdzqSqlSessionFactory")
    public SqlSessionFactory jdzqSqlSessionFactory(@Qualifier("jdzqDataSource") DataSource jdzqDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(jdzqDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
