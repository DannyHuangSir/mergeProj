package com.twfhclife.generic.configure;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/***
 * MyBatis configuration.
 * 
 * @author All
 * @version 1.0
 */
//@Configuration
//@MapperScan(basePackages = { 
//		"com.twfhclife.adm.dao",
//		"com.twfhclife.keycloak.dao"}, sqlSessionFactoryRef = "esadmSqlSessionFactory")
public class EserviceDatasourceConfig2 {

	private static final Logger logger = LogManager.getLogger(EserviceDatasourceConfig2.class);

	/** mybatis 配置路徑 */
	private static String MYBATIS_CONFIG = "/mybatis/mybatis-config.xml";
	/** mybatis mapper resource 路徑 */
	private static String MAPPER_PATH = "/mybatis/mapper/**/*.xml";

	@Value("${spring.datasource.es.jndi-name}")
	private String esJNDIName;

	@Value("${spring.datasource.es.embeddedjndi}")
	private String embeddedjndi;

	//private String typeAliasPackage = "com.aoshi.domain";

	/**
	 * 创建sqlSessionFactoryBean 实例 并且设置configtion 如驼峰命名.等等 设置mapper 映射路径
	 * 设置datasource数据源
	 * 
	 * @return
	 */
	//	@Bean
	//	public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
	//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	//		/** 设置mybatis configuration 扫描路径 */
	//		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
	//		/** 添加mapper 扫描路径 */
	//		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
	//		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + MAPPER_PATH;
	//		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
	//		/** 设置datasource */
	//		sqlSessionFactoryBean.setDataSource(dataSource);
	//		/** 设置typeAlias 包扫描路径 */
	//		//sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
	//		return sqlSessionFactoryBean;
	//	}

	/* JNDI DataSource Setting for Deploy */
	@Bean(name = "esDataSource", destroyMethod = "")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.es")
	public DataSource dataSource() {
		DataSource ds ;
		if ("false".equals(embeddedjndi)) {
			Long stsrtT =System.currentTimeMillis();
			//System.out.println("ICE DB CreateDbConnection Start"+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date(stsrtT)));
			ds = DataSourceBuilder.create().build();
			//System.out.println("CreateDbConnection SPEND TIME : " + (System.currentTimeMillis() - stsrtT) + " ms");
		} else {
			logger.debug("start to get JNDIName="+esJNDIName);
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			ds = dataSourceLookup.getDataSource(esJNDIName);
			logger.debug("end to get JNDIName="+esJNDIName);
		}
		return ds;
//		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
//		jndiObjectFactoryBean.setJndiName("jdbc/jndidatasource");
//		try {
//			jndiObjectFactoryBean.afterPropertiesSet();
//		} catch (NamingException e) {
//			logger.error("Error while retrieving datasource with JNDI name jdbc/jndidatasource", e);
//		}
//		return (DataSource) jndiObjectFactoryBean.getObject();
	}

	@Primary
	@Bean(name = "esadmTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("esadmDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "esadmSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("esadmDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

		/** 設定mybatis configuration 掃描路徑 */
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
		/** 添加mapper 掃描路徑 */
//		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + MAPPER_PATH;
//		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(MAPPER_PATH));
		/** 設定datasource */
		sqlSessionFactoryBean.setDataSource(dataSource);
		/** 設定typeAlias 掃描路徑 */
		// sqlSessionFactoryBean.setTypeAliasesPackage("com.fet.ice.generic.model");

		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean.getObject();
	}
}