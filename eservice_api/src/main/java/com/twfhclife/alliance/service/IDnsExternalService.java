package com.twfhclife.alliance.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * 死亡除戶
 * 呼叫聯盟Service
 *
 */
public interface IDnsExternalService {

	/**
	 * 
	 * @param url String
	 * @param params Map<String, String>
	 * @return String jsonType
	 * @throws Exception
	 */
	String postForEntity(String url,Map<String, String> params,Map<String, String> unParams) throws Exception;

	/**
	 * 推送核心
	 * @param url
	 * @param params
	 * @param unParams
	 * @return
	 * @throws Exception
	 */

	String postCoreEntity(String url,Map<String, String> params,Map<String, String> unParams) throws Exception;

	/**
	 * 檢查http response status
	 * @param responseEntity
	 * @return
	 */
	boolean checkResponseStatus(ResponseEntity<?> responseEntity);
	
}
