package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.BenefitVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

