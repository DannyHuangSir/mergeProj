package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.dao.*;
import com.twfhclife.es.dao.MyEserviceTestDao;
import com.twfhclife.esjd.dao.MyEserviceJDTestDao;
import com.twfhclife.shouxian.dao.MyShouxianTestDao;
import com.twfhclife.uderwriting.dao.MyUnderwritingTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.service.IMyTestService;
import com.twfhclife.es.dao.MyTestDao;
import com.twfhclife.generic.util.SqlUtil;


@Service
public class MyTestServiceImpl implements IMyTestService {

	@Autowired
	private MyTestDao myTestDao;
	
	@Autowired
	private MyAdmTestDao myAdmTestDao;

	@Autowired
	private MyEserviceTestDao myEserviceTestDao;

	@Autowired
	private MyEserviceJDTestDao myEserviceJDTestDao;

	@Autowired
	private MyUnderwritingTestDao myUnderwritingTestDao;

	@Autowired
	private MyShouxianTestDao myShouxianTestDao;
	
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

	@Override
	public List<Map<String, Object>> getEserviceResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)){
			throw new Exception("Invalid SQL Script.");
		}
		return myEserviceTestDao.doQuery(script);
	}

	@Override
	public List<Map<String, Object>> getEserviceJdResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)){
			throw new Exception("Invalid SQL Script.");
		}
		return myEserviceJDTestDao.doQuery(script);
	}

	@Override
	public List<Map<String, Object>> getUnderwritingResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)){
			throw new Exception("Invalid SQL Script.");
		}
		return myUnderwritingTestDao.doQuery(script);
	}

	@Override
	public List<Map<String, Object>> getShouxianResult(String script) throws Exception {
		if(!SqlUtil.checkSqlInjection(script)){
			throw new Exception("Invalid SQL Script.");
		}
		return myShouxianTestDao.doQuery(script);
	}
}
