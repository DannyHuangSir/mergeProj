package com.twfhclife.eservice.dashboard.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 保障總覽服務.
 * 
 * @author alan
 */
public interface IDashboardService {
	
	/**
	 * 取得被保人的商品類型清單.
	 * 
	 * @param benefitPolicyList 保障型保單資料
	 * @return 回傳被保人的商品資料
	 */
	public Map<String, List<String>> getInsuredProductData(List<PolicyListVo> benefitPolicyList);
	
	/**
	 * 取得保障型的的被保人保項資料.
	 * 
	 * @param benefitPolicyList 保障型保單資料
	 * @return 回傳被保人保項資料
	 */
	public Map<String, List<CoverageVo>> getBenefitInsuredData(List<PolicyListVo> benefitPolicyList);
	
	/**
	 * 取得參考帳戶合計價值(換算成台幣)
	 * @param invtPolicyList
	 * @return BigDecimal
	 */
	public BigDecimal getPolicyAcctValueTotal(List<PolicyListVo> invtPolicyList); 
}
