package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.ExchangeRateVo;

/**
 * 匯率 Dao.
 * 
 * @author all
 */
public interface ExchangeRateDao {
	
	/**
	 * 匯率-查詢.
	 * 
	 * @param exchangeRateVo ExchangeRateVo
	 * @return 回傳查詢結果
	 */
	List<ExchangeRateVo> getExchangeRate(@Param("exchangeRateVo") ExchangeRateVo exchangeRateVo);
	
	/**
	 * 以EXCHANGE_CODE查詢CURRENCY_CODE=NTD最新匯率
	 * @param exchangeCode
	 * @return List<ExchangeRateVo>
	 */
	List<ExchangeRateVo> getNewestExchangeRateByExchangeCode(@Param("exchangeCode") String exchangeCode);
}