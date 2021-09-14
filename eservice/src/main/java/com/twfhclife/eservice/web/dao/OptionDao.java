package com.twfhclife.eservice.web.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OptionDao {

	public List<Map<String, Object>> getCityList();

	public List<Map<String, Object>> getRegionList(@Param("cityId") String cityId);

	public List<Map<String, Object>> getRoadList(@Param("regionId") String regionId);

	public List<Map<String, Object>> getBankList();
	
	public List<Map<String, Object>> getBranchesList(@Param("bankId") String bankId);

	public List<Map<String, Object>> getTransactionCodeList(@Param("systemId") String systemId,
			@Param("categoryCode") String categoryCode);
	
	public List<Map<String, Object>> getAreaList();

	public List<Map<String, Object>> getCountryList(@Param("areaId") String areaId);
	
	public List<Map<String, Object>> getJobList1();
	
	public List<Map<String, Object>> getJobList2(@Param("jobB") String jobB);
	
	public List<Map<String, Object>> getJobList3(@Param("jobB") String jobB, @Param("jobM") String jobM);

	List<Map<String, String>> getCompanysList();

	List<Map<String, String>> getCurrencysList();
}
