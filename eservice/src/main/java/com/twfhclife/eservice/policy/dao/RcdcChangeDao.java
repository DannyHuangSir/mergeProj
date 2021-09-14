package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.RcdcChangeVo;

/**
 * 保單紅利 Dao.
 * 
 * @author all
 */
public interface RcdcChangeDao {
	
	/**
	 * 契約變更-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<RcdcChangeVo> findByPolicyNo(@Param("policyNo") String policyNo);
	
}