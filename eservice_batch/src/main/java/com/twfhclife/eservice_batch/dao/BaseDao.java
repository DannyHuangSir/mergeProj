package com.twfhclife.eservice_batch.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseDao {

	private static final Logger logger = LogManager.getLogger(BaseDao.class);

	private SqlSession sqlSession;

	private SqlSessionFactory getSessionFactory() {
		SqlSessionFactory sessionFactory = null;
		String resource = "mybatis-config.xml";
		try {
			sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
		} catch (Exception e) {
			logger.error("getSqlSessionFactory error:", e);
		}
		return sessionFactory;
	}

	public SqlSession getSqlSession() {
		if (sqlSession == null) {
			sqlSession = getSessionFactory().openSession();
			logger.info("Get SqlSession success.");
		}
		return sqlSession;
	}

	public void release() {
		if (sqlSession != null) {
			sqlSession.close();
			sqlSession = null;
		}
		logger.info("SqlSession closed.");
	}
	
	
	public void commit() {
		if (sqlSession != null) {
			sqlSession.commit();
		}
		logger.info("SqlSession committed.");
	}
	
	public void rollback() {
		if (sqlSession != null) {
			sqlSession.rollback();
		}
		logger.info("SqlSession rollbacked.");
	}

}
