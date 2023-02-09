package com.twfhclife.eservice.service;

import com.twfhclife.eservice.web.model.CoverageVo;

import java.util.List;

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