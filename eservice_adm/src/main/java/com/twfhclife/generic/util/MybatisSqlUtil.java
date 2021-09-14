package com.twfhclife.generic.util;

import java.util.List;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MybatisSqlUtil {

	private static final Logger logger = LoggerFactory.getLogger(MybatisSqlUtil.class);
	
	@Autowired
	private SqlSessionFactory sessionFactory;
	
	public String getNativeSql(String sqlId, Object param) {
		MappedStatement ms = sessionFactory.getConfiguration().getMappedStatement(sqlId);
		BoundSql boundSql = ms.getBoundSql(param);
		
		String nativeSql = boundSql.getSql();
		List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
		for (ParameterMapping mapping : paramMapping) {
			String propValue = mapping.getProperty();
			
			Object v = null;
			try {
				v = new JexlEngine().getProperty(param, propValue);
			} catch (JexlException e) {
				logger.warn(String.format(
						"SKIP PROPERTY - [%s]. (no such public getter or field on parameter object.).", propValue), e);
				continue;
			}

			Class<?> javaType = mapping.getJavaType();
			if (String.class == javaType) {
				nativeSql = nativeSql.replaceFirst("\\?", "'" + v + "'");
			} else if (Object.class == javaType) {
				if (isInteger(v)) {
					nativeSql = nativeSql.replaceFirst("\\?", (v == null ? "" : v.toString()));
				} else {
					nativeSql = nativeSql.replaceFirst("\\?", "'" + v + "'");
				}
			} else {
				nativeSql = nativeSql.replaceFirst("\\?", (v == null ? "" : v.toString()));
			}
		}
		
		String unformatSql = (nativeSql == null ? "" : nativeSql.replaceAll("[\\s\n ]+", " "));
		logger.info("[{}] running sql: {}", sqlId, unformatSql);
		
		return unformatSql;
	}
	
	private boolean isInteger(Object v) {
		if (v == null) {
			return false;
		}
		
		if (v instanceof Integer) {
			return true;
		}
		
		try {
			Integer.parseInt((String) v);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
