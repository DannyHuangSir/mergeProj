package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.RcdcChangeVo;

/**
 * 契約變更資料.
 * 
 * @author all
 */
public interface IRcdcChangeService {
	

	/**
	 * 契約變更資料-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public List<RcdcChangeVo> findByPolicyNo(String policyNo);
	
}