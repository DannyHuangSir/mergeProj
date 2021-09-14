package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.CoverageVo;

/**
 * 保單保項服務.
 * 
 * @author all
 */
public interface ICoverageService {
	
	/**
	 * 保單保項服務-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	public List<CoverageVo> getCoverageList(CoverageVo coverageVo);

	/**
	 * 主約被保人保項-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	public CoverageVo getInsuredCoverage(CoverageVo coverageVo);
}