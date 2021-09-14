package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;

public interface PolicyAccountValueDao {

	/**
	 * 查詢保單帳戶價值
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	List<PolicyAccountValueVo> getPolicyAccountValueList(@Param("policyNo") String policyNo);
	
	/**
	 * 查詢每季保單帳戶價值
	 * 
	 * @param policyNo 保單號碼
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @return
	 */
	List<PolicyAccountValueVo> getQuaterPolicyAccountValueList(@Param("policyNo") String policyNo,
			@Param("startDate") String startDate, @Param("endDate") String endDate);
}
