package com.twfhclife.generic.configure;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = ShouXianMybatisConfig.PACKAGE, sqlSessionFactoryRef = "shouxianSqlSessionFactory")
public class ShouXianMybatisConfig {

    static final String PACKAGE = "com.twfhclife.eservice.api.shouxian.dao";
    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/shouxian/*.xml";

    @Value("${shouxian.datasource.embeddedjndi}")
    private String embeddedjndi;
    @Value("${shouxian.datasource.jndi-name}")
    private String shouxianJNDIName;

    @Bean(name = "shouxianDataSource", destroyMethod = "")
    @ConfigurationProperties(prefix = "shouxian.datasource")
    public DataSource shouxianDataSource() {
        DataSource ds;
        if (StringUtils.equals(embeddedjndi, "false")) {
            ds = DataSourceBuilder.create().build();
            return ds;
        } else {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            return dataSourceLookup.getDataSource(shouxianJNDIName);
        }
    }
    @Bean(name = "shouxianTransactionManager")
    public DataSourceTransactionManager shopTransactionManager() {
        return new DataSourceTransactionManager(shouxianDataSource());
    }

    @Bean(name = "shouxianSqlSessionFactory")
    public SqlSessionFactory shouxianSqlSessionFactory(@Qualifier("shouxianDataSource") DataSource shouxianDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(shouxianDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
