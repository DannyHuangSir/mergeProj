package com.twfhclife.generic.configure;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

/***
 * MyBatis configuration.
 * 
 * @author All
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = { "com.twfhclife.esjd.dao" }, sqlSessionFactoryRef = "esjdSqlSessionFactory")
public class EserviceJDDatasourceConfig {

	private static final Logger logger = LogManager.getLogger(EserviceJDDatasourceConfig.class);

	@Value("${spring.datasource.esjd.jndi-name}")
	private String esJNDIName;

	@Value("${spring.datasource.esjd.embeddedjndi}")
	private String embeddedjndi;

	/* JNDI DataSource Setting for Deploy */
	@Bean(name = "esjdDataSource", destroyMethod = "")
	@ConfigurationProperties(prefix = "spring.datasource.esjd")
	public DataSource iceDataSource() {
		// System.out.println("iceJNDIName="+iceJNDIName);
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

	@Bean(name = "esjdTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("esjdDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "esjdSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("esjdDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		//factoryBean.setTypeAliasesPackage("com.fet.ice.generic.model");
		return factoryBean.getObject();
	}
}
