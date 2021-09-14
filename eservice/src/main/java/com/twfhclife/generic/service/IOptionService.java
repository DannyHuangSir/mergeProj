package com.twfhclife.generic.service;

import java.util.List;
import java.util.Map;

public interface IOptionService {
	
	public List<Map<String, Object>> getCityList();
	
	public List<Map<String, Object>> getRegionList(String cityId);
	
	public List<Map<String, Object>> getRoadList(String regionId);
	
	public List<Map<String, Object>> getBankList();
	
	public List<Map<String, Object>> getBranchesList(String bankId);
	
	public List<Map<String, Object>> getTransactionCodeList();
	
	public List<Map<String, Object>> getTransStatusList();
	
	public List<Map<String, Object>> getTransTypeList();

	public List<Map<String, Object>> getAreaList();

	public List<Map<String, Object>> getCountryList(String areaId);
	
	public List<Map<String, Object>> getJobList1();
	
	public List<Map<String, Object>> getJobList2(String jobB);
	
	public List<Map<String, Object>> getJobList3(String jobB, String jobM);
	
}
