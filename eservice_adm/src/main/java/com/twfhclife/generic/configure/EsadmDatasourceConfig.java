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

/***
 * MyBatis configuration.
 * 
 * @author All
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = { "com.twfhclife.adm.dao", "com.twfhclife.keycloak.dao" }, sqlSessionFactoryRef = "esadmSqlSessionFactory")
public class EsadmDatasourceConfig {

	private static final Logger logger = LogManager.getLogger(EsadmDatasourceConfig.class);
	
	/** mybatis 配置路徑 */
	@Value("${spring.datasource.adm.mybatis.config-locations}")
	private String MYBATIS_CONFIG;
	/** mybatis mapper resource 路徑 */
	@Value("${spring.datasource.adm.mybatis.mapper-locations}")
	private String MAPPER_PATH;

	@Value("${spring.datasource.adm.jndi-name}")
	private String esadmJNDIName;

	@Value("${spring.datasource.adm.embeddedjndi}")
	private String embeddedjndi;

	/* JNDI DataSource Setting for Deploy */
	@Bean(name = "esadmDataSource", destroyMethod = "")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.adm")
	public DataSource esadmDataSource() {
		DataSource ds;
		if (StringUtils.equals(embeddedjndi, "false")) {
			ds = DataSourceBuilder.create().build();
			return ds;
		} else {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(esadmJNDIName);
		}
	}

	@Bean(name = "esadmTransactionManager")
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier("esadmDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "esadmSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("esadmDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		/** mybatis configuration掃描路徑 */
		factoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
		/** mapper掃描路徑 */
//		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//		factoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(MAPPER_PATH));
		factoryBean.setDataSource(dataSource);
		//factoryBean.setTypeAliasesPackage("com.fet.ice.generic.model");
		return factoryBean.getObject();
	}
}