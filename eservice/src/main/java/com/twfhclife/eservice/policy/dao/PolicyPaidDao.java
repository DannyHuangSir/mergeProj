package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.model.PolicyPaidVo;

/**
 * 保單紅利 Dao.
 * 
 * @author all
 */
public interface PolicyPaidDao {
	
	/**
	 * 保單紅利-查詢.
	 * 
	 * @param policyBonusVo PolicyBonusVo
	 * @return 回傳查詢結果
	 */
	List<PolicyPaidVo> getPolicyPaid(@Param("policyPaidVo") PolicyPaidVo policyPaidVo);
	
	/**
	 * 保單紅利-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<PolicyPaidVo> findByPolicyNo(@Param("policyNo") String policyNo);
	
	/**
	 * 查詢紅利選擇權.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳紅利選擇權
	 */
	String getBounsTake(@Param("policyNo") String policyNo);
}