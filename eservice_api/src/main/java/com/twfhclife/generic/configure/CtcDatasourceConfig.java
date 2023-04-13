package com.twfhclife.generic.configure;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
@MapperScan(basePackages = {"com.twfhclife.eservice.api.elife.dao"}, sqlSessionFactoryRef = "ctcSqlSessionFactory")
public class CtcDatasourceConfig {

	private static final Logger logger = LogManager.getLogger(CtcDatasourceConfig.class);

	/** mybatis 配置路徑 */
	@Value("${spring.datasource.ctc.mybatis.config-locations}")
	private String MYBATIS_CONFIG;
	/** mybatis mapper resource 路徑 */
	@Value("${spring.datasource.ctc.mybatis.mapper-locations}")
	private String MAPPER_PATH;
	
	@Value("${spring.datasource.ctc.jndi-name}")
	private String esJNDIName;

	@Value("${spring.datasource.ctc.embeddedjndi}")
	private String embeddedjndi;

	/* JNDI DataSource Setting for Deploy */
	@Bean(name = "ctcDataSource", destroyMethod = "")
	@ConfigurationProperties(prefix = "spring.datasource.ctc")
	public DataSource ctcDataSource() {
		logger.debug("CtcDatasourceConfig JNDI=" + esJNDIName);
		DataSource ds;
		if (StringUtils.equals(embeddedjndi, "false")) {
			ds = DataSourceBuilder.create().build();
			return ds;
		} else {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(esJNDIName);
		}
	}

	/* DataSource Setting for Local test */
//	 @Bean(name = "esDataSource")
//	 @ConfigurationProperties(prefix = "spring.datasource.es")
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().build();
//	}

	@Bean(name = "ctcTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("ctcDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "ctcSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("ctcDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		/** mybatis configuration掃描路徑 */
		factoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
		/** mapper掃描路徑 */
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//		factoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(MAPPER_PATH));
		factoryBean.setDataSource(dataSource);
		//factoryBean.setTypeAliasesPackage("com.fet.ice.generic.model");
		return factoryBean.getObject();
	}
}
