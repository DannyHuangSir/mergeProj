package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.FundTransactionDao;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.service.IFundTransactionService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 交易歷史記錄服務.
 * 
 * @author all
 */
@Service
public class FundTransactionServiceImpl implements IFundTransactionService {

	@Autowired
	private FundTransactionDao fundTransactionDao;

	/**
	 * 交易歷史記錄-分頁查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @param trCode 交易類別
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保交易歷史記錄
	 */
	@RequestLog
	@Override
	public List<FundTransactionVo> getFundTransactionPageList(String policyNo, String trCode, String startDate,
			String endDate, int pageNum, int pageSize) {
		return fundTransactionDao.getFundTransactionPageList(policyNo, trCode, startDate, endDate, pageNum, pageSize);
	}

	/**
	 * 交易歷史記錄-查詢.
	 * 
	 * @param fundTransactionVo FundTransactionVo
	 * @return 回傳保交易歷史記錄
	 */
	@RequestLog
	@Override
	public List<FundTransactionVo> getFundTransactionList(FundTransactionVo fundTransactionVo) {
		return fundTransactionDao.getFundTransactionList(fundTransactionVo);
	}
}