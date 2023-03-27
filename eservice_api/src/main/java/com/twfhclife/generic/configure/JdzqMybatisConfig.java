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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = JdzqMybatisConfig.PACKAGE, sqlSessionFactoryRef = "jdzqSqlSessionFactory")
public class JdzqMybatisConfig {

    static final String PACKAGE = "com.twfhclife.eservice.api.jdzq.dao";
    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/jdzq/*.xml";

    @Value("${jdzq.datasource.embeddedjndi}")
    private String embeddedjndi;
    @Value("${jdzq.datasource.jndi-name}")
    private String jdJNDIName;

    @Bean(name = "jdzqDataSource")
    @ConfigurationProperties(prefix = "jdzq.datasource")
    public DataSource jdzqDataSource() {
        DataSource ds;
        if (StringUtils.equals(embeddedjndi, "false")) {
            ds = DataSourceBuilder.create().build();
            return ds;
        } else {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            return dataSourceLookup.getDataSource(jdJNDIName);
        }
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
