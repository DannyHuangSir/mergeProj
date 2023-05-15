package com.twfhclife.jd.configure;

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

import static com.twfhclife.jd.configure.MybatisConfig.PACKAGE;


@Configuration
@MapperScan(basePackages = PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {

    static final String PACKAGE = "com.twfhclife.jd.web.dao";
    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/eservice/*.xml";

    @Value("${spring.datasource.jndi-name}")
    private String esJNDIName;

    @Value("${spring.datasource.embeddedjndi}")
    private String embeddedjndi;


    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DataSource ds;
        if (StringUtils.equals(embeddedjndi, "false")) {
            ds = DataSourceBuilder.create().build();
            return ds;
        } else {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            return dataSourceLookup.getDataSource(esJNDIName);
        }
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
