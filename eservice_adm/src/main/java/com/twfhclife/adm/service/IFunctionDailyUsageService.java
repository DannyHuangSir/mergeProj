package com.twfhclife.adm.service;

/**
 * 功能使用統計 service
 * @author Ken Wei
 *
 */
public interface IFunctionDailyUsageService {

	/**
	 * 功能使用統計次數
	 * @param funcId
	 * @param systemId
	 * @return
	 */
	public int addFunctionUsageCount(String funcId, String systemId);
}
