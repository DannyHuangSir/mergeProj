package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.BenefitVo;

/**
 * 保障項目 Dao.
 * 
 * @author all
 */
public interface BenefitDao {
	
	/**
	 * 保障項目-查詢.
	 * 
	 * @param benefitVo BenefitVo
	 * @param String policyNo
	 * @return 回傳查詢結果
	 */
	List<BenefitVo> getBenefitList(@Param("benefitVo") BenefitVo benefitVo, @Param("policyNo") String policyNo);
}

