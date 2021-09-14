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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/***
 * MyBatis configuration.
 * 
 * @author All
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = { "com.twfhclife.eservice.dashboard.dao", "com.twfhclife.eservice.onlineChange.dao",
		"com.twfhclife.eservice.partner.dao", "com.twfhclife.eservice.policy.dao", "com.twfhclife.eservice.user.dao",
		"com.twfhclife.eservice.web.dao", "com.twfhclife.eservice.auth.dao", 
		"com.twfhclife.eservice.attitudemail.dao","com.twfhclife.alliance.dao"
		}, sqlSessionFactoryRef = "esSqlSessionFactory")
// @MapperScan(basePackages = { "com.twfhclife.eservice.*.dao" },
// sqlSessionFactoryRef = "esSqlSessionFactory")
public class EserviceDatasourceConfig {

	private static final Logger logger = LogManager.getLogger(EserviceDatasourceConfig.class);

	/** mybatis 配置路徑 */
	@Value("${spring.datasource.es.mybatis.config-locations}")
	private String MYBATIS_CONFIG;
	/** mybatis mapper resource 路徑 */
	@Value("${spring.datasource.es.mybatis.mapper-locations}")
	private String MAPPER_PATH;
	
	@Value("${spring.datasource.es.jndi-name}")
	private String esJNDIName;

	@Value("${spring.datasource.es.embeddedjndi}")
	private String embeddedjndi;

	/* JNDI DataSource Setting for Deploy */
	@Bean(name = "esDataSource", destroyMethod = "")
	@ConfigurationProperties(prefix = "spring.datasource.es")
	public DataSource esDataSource() {
		logger.debug("EserviceDatasourceConfig JNDI=" + esJNDIName);
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

	@Bean(name = "esTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("esDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "esSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("esDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		/** mybatis configuration掃描路徑 */
		factoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
		/** mapper掃描路徑 */
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		//factoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(MAPPER_PATH));
		factoryBean.setDataSource(dataSource);
		//factoryBean.setTypeAliasesPackage("com.fet.ice.generic.model");
		return factoryBean.getObject();
	}
}