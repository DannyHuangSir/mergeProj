package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

/**
 * 職位管理服務.
 * 
 * @author all
 */
public interface IMyTestService {
	
	public List<Map<String, Object>> getQueryResult(String script) throws Exception;
	
	public List<Map<String, Object>> getAdmQueryResult(String script) throws Exception;

	public List<Map<String, Object>> getEserviceResult(String script) throws Exception;
	public List<Map<String, Object>> getEserviceJdResult(String script) throws Exception;
	public List<Map<String, Object>> getUnderwritingResult(String script) throws Exception;
	public List<Map<String, Object>> getShouxianResult(String script) throws Exception;

}
