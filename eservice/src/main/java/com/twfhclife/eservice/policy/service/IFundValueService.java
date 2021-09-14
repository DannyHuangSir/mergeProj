package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.FundValueVo;

/**
 * 淨值服務.
 * 
 * @author all
 */
public interface IFundValueService {
	
	/**
	 * 淨值-查詢.
	 * 
	 * @param fundValueVo FundValueVo
	 * @return 回傳查詢結果
	 */
	public List<FundValueVo> getFundValue(FundValueVo fundValueVo);
}