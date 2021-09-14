package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PolicyDividendVo;

/**
 * 保單收益分配 Dao.
 * 
 * @author all
 */
public interface PolicyDividendDao {
	
	/**
	 * 保單收益分配-查詢.
	 * 
	 * @param policyDividendVo PolicyDividendVo
	 * @return 回傳查詢結果
	 */
	List<PolicyDividendVo> getPolicyDividend(@Param("policyDividendVo") PolicyDividendVo policyDividendVo);
}

