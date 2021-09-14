package com.twfhclife.eservice.policy.dao;

import com.twfhclife.eservice.policy.model.PolicyPaidVo;
import com.twfhclife.eservice.policy.model.PolicyReversalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保單撥回 Dao.
 * 
 * @author all
 */
public interface PolicyReversalDao {

	/**
	 * 保單撥回-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<PolicyReversalVo> findByPolicyNo(@Param("policyNo") String policyNo);
	


}