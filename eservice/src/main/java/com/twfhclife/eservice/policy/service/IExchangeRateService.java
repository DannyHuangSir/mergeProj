package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.ExchangeRateVo;

/**
 * 匯率服務.
 * 
 * @author all
 */
public interface IExchangeRateService {
	
	/**
	 * 匯率-查詢.
	 * 
	 * @param exchangeRateVo ExchangeRateVo
	 * @return 回傳查詢結果
	 */
	public List<ExchangeRateVo> getExchangeRate(ExchangeRateVo exchangeRateVo);
	
	/**
	 * 以EXCHANGE_CODE查詢CURRENCY_CODE=NTD最新匯率
	 * @param exchangeCode
	 * @return List<ExchangeRateVo>
	 */
	public List<ExchangeRateVo> getNewestExchangeRateByExchangeCode(String exchangeCode);
}