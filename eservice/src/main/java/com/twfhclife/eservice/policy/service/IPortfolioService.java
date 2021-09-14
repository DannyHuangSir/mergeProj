package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;

public interface IPortfolioService {
	
	/**
	 * 取得投資損益及投報率清單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資損益及投報率清單資料
	 */
	public List<PortfolioVo> getPortfolioList(String policyNo, List<String> invtNo);
	
	/**
	 * 設定平均報酬率及帳戶價值資料.
	 * 
	 * @param invtPolicyList 投資型保單料
	 */
	public void setPortfolioData(List<PolicyListVo> invtPolicyList);
	
	/**
	 * 取得基金下拉選單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳基金下拉選單資料
	 */
	public List<PortfolioVo> getInvtOptionList(String policyNo);
	
	/**
	 * 取得投資風險屬性.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資風險屬性
	 */
	public String getRiskLevelName(String policyNo);

}