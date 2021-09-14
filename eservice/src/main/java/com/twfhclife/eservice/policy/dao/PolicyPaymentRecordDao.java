package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;

/**
 * 繳費 Dao.
 * 
 * @author all
 */
public interface PolicyPaymentRecordDao {
	
	/**
	 * 繳費-查詢.
	 * 
	 * @param policyPaymentRecordVo PolicyPaymentRecordVo
	 * @return 回傳查詢結果
	 */
	List<PolicyPaymentRecordVo> getPolicyPaymentRecord(
			@Param("policyPaymentRecordVo") PolicyPaymentRecordVo policyPaymentRecordVo);
	
	/**
	 * 繳費(最近一年)-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<PolicyPaymentRecordVo> getPolicyPaymentRecordLastYear(@Param("policyNo") String policyNo);

}
