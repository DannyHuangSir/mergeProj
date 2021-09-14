package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.web.model.BenefitVo;

/**
 * 保障項目服務.
 * 
 * @author all
 */
public interface IBenefitService {
	
	/**
	 * 保障項目-查詢.
	 * 
	 * @param benefitVo BenefitVo
	 * @return 回傳查詢結果
	 */
	public List<BenefitVo> getBenefitList(BenefitVo benefitVo);
}