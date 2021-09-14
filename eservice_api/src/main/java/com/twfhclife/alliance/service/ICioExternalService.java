package com.twfhclife.alliance.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.ContactInfoParameterVo;

public interface ICioExternalService {
	
	/**
	 * 
	 * @param url String
	 * @param params Map<String, String>
	 * @return String jsonType
	 * @throws Exception
	 */
	String postForEntity(String url,Map<String, String> params,Map<String, String> unParams) throws Exception;
	/**
	 *
	 * @param url String
	 * @param params Map<String, Object>
	 * @return String jsonType
	 * @throws Exception
	 */
	String postForMapEntity(String url,Map<String, Object> params,Map<String, String> unParams) throws Exception;

	/**
	 * 
	 * @param url
	 * @param cliaimVo
	 * @return String jsonType
	 * @throws Exception
	 */
	String postForEntity(String url,ContactInfoParameterVo cliaimVo, String name) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<String> getPolicyNoByID(String id);
	
	/**
	 * 依保單號碼找出商品險種
	 * @param policyNo
	 * @return List<String>
	 */
	List<String> getProductCodeByPolicyNo(@Param("policyNo") String policyNo);

	/***
	 * 更新聯盟狀態
	 * @param boo
	 * @param unParams
	 * @return
	
	public  int  insertUnionCourseVo(boolean boo,Map<String, String> unParams);
	 */
}
