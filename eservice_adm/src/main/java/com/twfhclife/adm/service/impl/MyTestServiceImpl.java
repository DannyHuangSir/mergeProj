package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.MyAdmTestDao;
import com.twfhclife.adm.service.IMyTestService;
import com.twfhclife.es.dao.MyTestDao;
import com.twfhclife.generic.util.SqlUtil;


@Service
public class MyTestServiceImpl implements IMyTestService {

	@Autowired
	private MyTestDao myTestDao;
	
	@Autowired
	private MyAdmTestDao myAdmTestDao;
	
	@Override
	public List<Map<String, Object>> getQueryResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)) {
			throw new Exception("Invalid SQL Script.");
		}
		return myTestDao.doQuery(script);
	}

	@Override
	public List<Map<String, Object>> getAdmQueryResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)) {
			throw new Exception("Invalid SQL Script.");
		}
		return myAdmTestDao.doQuery(script);
	}
}