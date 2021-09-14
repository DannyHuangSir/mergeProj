package com.twfhclife.eservice.partner.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.partner.model.PartnerVo;

public interface PartnerDao {

	/**
	 * 取得保代下的使用者的所有保單清單.
	 * 
	 * @param rocId 要保人身份證字號
	 * @param policyNo 保單號碼
	 * @param agentCode 代理人代碼
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保代下的使用者的所有保單清單
	 */
	public List<PartnerVo> getPartnerPolicyPageList(
			@Param("rocId") String rocId, 
			@Param("policyNo") String policyNo,
			@Param("agentCode") String agentCode, 
			@Param("pageNum") int pageNum, 
			@Param("pageSize") int pageSize);
}