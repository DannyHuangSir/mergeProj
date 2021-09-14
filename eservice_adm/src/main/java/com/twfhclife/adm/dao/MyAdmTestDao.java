package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "myAdmTestDao")
public interface MyAdmTestDao {

	@SelectProvider(type = TestDaoProvider.class, method = "doQuery")
	public List<Map<String, Object>> doQuery(String script);

	class TestDaoProvider {
		public String doQuery(String script) {
			String sql = script;
			return sql;
		}
	}
}
