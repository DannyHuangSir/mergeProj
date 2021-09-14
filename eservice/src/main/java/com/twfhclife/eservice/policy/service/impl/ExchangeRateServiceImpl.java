package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.ExchangeRateDao;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.service.IExchangeRateService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 匯率服務.
 * 
 * @author all
 */
@Service
public class ExchangeRateServiceImpl implements IExchangeRateService {

	@Autowired
	private ExchangeRateDao exchangeRateDao;
	
	/**
	 * 匯率-查詢.
	 * 
	 * @param exchangeRateVo ExchangeRateVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<ExchangeRateVo> getExchangeRate(ExchangeRateVo exchangeRateVo) {
		return exchangeRateDao.getExchangeRate(exchangeRateVo);
	}

	@Override
	public List<ExchangeRateVo> getNewestExchangeRateByExchangeCode(String exchangeCode) {
		return exchangeRateDao.getNewestExchangeRateByExchangeCode(exchangeCode);
	}
}