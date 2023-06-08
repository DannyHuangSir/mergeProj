package com.twfhclife.alliance.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.alliance.model.SignInsuranceClaimMapperVo;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import org.springframework.http.HttpHeaders;

public interface IExternalService {
	
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
	 * @param url
	 * @param cliaimVo
	 * @return String jsonType
	 * @throws Exception
	 */
	String postForEntity(String url,InsuranceClaimMapperVo cliaimVo, String name) throws Exception;

	String postForEntity(String url, SignInsuranceClaimMapperVo cliaimVo, String name) throws Exception;

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

	String postForJson(String api417Url, HttpHeaders headers, Map<String, String> params) throws Exception;

	String postApi416(String api416Url, HttpHeaders headers, Map<String, String> api416Params) throws Exception;

	String postApi418(String urlApi108, Map<String, String> api108Params) throws Exception;
}
