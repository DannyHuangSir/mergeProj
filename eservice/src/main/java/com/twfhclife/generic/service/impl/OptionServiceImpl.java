package com.twfhclife.generic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.web.dao.OptionDao;
import com.twfhclife.generic.service.IOptionService;
import com.twfhclife.generic.util.ApConstants;

@Service
public class OptionServiceImpl implements IOptionService {

	@Autowired
	private OptionDao optionDao;

	@Override
	public List<Map<String, Object>> getCityList() {
		return optionDao.getCityList();
	}

	@Override
	public List<Map<String, Object>> getRegionList(String cityId) {
		return optionDao.getRegionList(cityId);
	}

	@Override
	public List<Map<String, Object>> getRoadList(String regionId) {
		return optionDao.getRoadList(regionId);
	}
	
	@Override
	public List<Map<String, Object>> getBankList() {
		return optionDao.getBankList();
	}
	
	@Override
	public List<Map<String, Object>> getBranchesList(String bankId) {
		return optionDao.getBranchesList(bankId);
	}

	@Override
	public List<Map<String, Object>> getTransactionCodeList() {
		return optionDao.getTransactionCodeList(ApConstants.SYSTEM_ID, ApConstants.TRANSACTION_PARAMETER_CATEGORY_CODE);
	}
	
	@Override
	public List<Map<String, Object>> getTransStatusList(){
		return optionDao.getTransactionCodeList(ApConstants.SYSTEM_ID, OnlineChangeUtil.ONLINE_CHANGE_STATUS_PARAMETER_CATEGORY_CODE);
	}
	
	@Override
	public List<Map<String, Object>> getTransTypeList(){
		return optionDao.getTransactionCodeList(ApConstants.SYSTEM_ID, OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE);
	}

	@Override
	public List<Map<String, Object>> getAreaList() {
		return optionDao.getAreaList();
	}

	@Override
	public List<Map<String, Object>> getCountryList(String areaId) {
		return optionDao.getCountryList(areaId);
	}

	@Override
	public List<Map<String, Object>> getJobList1() {
		return optionDao.getJobList1();
	}

	@Override
	public List<Map<String, Object>> getJobList2(String jobB) {
		return optionDao.getJobList2(jobB);
	}

	@Override
	public List<Map<String, Object>> getJobList3(String jobB, String jobM) {
		return optionDao.getJobList3(jobB, jobM);
	}
}