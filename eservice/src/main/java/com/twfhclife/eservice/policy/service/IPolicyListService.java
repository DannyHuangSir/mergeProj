package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.DepositPolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyVo;
import com.twfhclife.eservice.policy.model.ProductVo;

/**
 * 保單清單服務.
 * 
 * @author alan
 */
public interface IPolicyListService {
	
	/**
	 * 取得使用者的所有保單清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	public List<PolicyListVo> getUserPolicyList(String rocId);
	
	/**
	 * 取得使用者的所有保單清單(保單理賠).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	public List<PolicyListVo> getUserPolicyListByRocId(String rocId);

	/**
	 * 取得使用者的所有保單清單(投資型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */	
	public List<PolicyListVo> getInvtPolicyList(String rocId);

	/**
	 * 取得使用者的所有保單清單(保障型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */	
	public List<PolicyListVo> getBenefitPolicyList(String rocId);
	
	/**
	 * 取得我的保單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public PolicyVo findById(String policyNo);
	
	/**
	 * 取得product資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	public ProductVo findProductByPolicyNo(String policyNo);

	/***
	 * 获取投资型保单
	 * @param rocId
	 * @return
	 */
	public List<PolicyListVo> getInvestmentPolicyList(String rocId);

	/***
	 * 獲取可提領保單
	 * @param userRocId
	 * @param policyNo
	 * @return
	 */
	public List<PolicyListVo> getDepositList(String userRocId, String policyNo);
}