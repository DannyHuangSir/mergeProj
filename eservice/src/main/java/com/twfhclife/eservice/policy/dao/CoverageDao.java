package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.CoverageVo;

/**
 * 保單保項 Dao.
 * 
 * @author all
 */
public interface CoverageDao {
	
	/**
	 * 保單保項-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	List<CoverageVo> getCoverageList(@Param("coverageVo") CoverageVo coverageVo);

	/**
	 * 主約被保人保項-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	CoverageVo getInsuredCoverage(@Param("coverageVo") CoverageVo coverageVo);
}

