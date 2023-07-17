package com.twfhclife.uderwriting.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "myUnderwritingTestDao")
public interface MyUnderwritingTestDao {

	@SelectProvider(type = TestDaoProvider.class, method = "doQuery")
	public List<Map<String, Object>> doQuery(String script);

	class TestDaoProvider {
		public String doQuery(String script) {
			String sql = script;
			return sql;
		}
	}
}
