package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PolicyExtraVo;

/**
 * 保單貸款 Dao.
 * 
 * @author all
 */
public interface PolicyExtraDao {
	
	/**
	 * 保單貸款-查詢.
	 * 
	 * @param policyExtraVo PolicyExtraVo
	 * @return 回傳查詢結果
	 */
	List<PolicyExtraVo> getPolicyExtra(@Param("policyExtraVo") PolicyExtraVo policyExtraVo);
	
	/**
	 * 保單貸款-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	PolicyExtraVo findByPolicyNo(@Param("policyNo") String policyNo);

	/**
	 * 取得使用者的資產與負債.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	PolicyExtraVo getUserAllLoanData(@Param("rocId") String rocId);
	
	/**
	 * 取得繳費提醒.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	List<PolicyExtraVo> getPaymentReminderList(@Param("rocId") String rocId);
}