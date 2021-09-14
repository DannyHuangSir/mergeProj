package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.FundTransactionVo;

/**
 * 交易歷史記錄服務.
 * 
 * @author all
 */
public interface IFundTransactionService {

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
	public List<FundTransactionVo> getFundTransactionPageList(String policyNo, String trCode, String startDate,
			String endDate, int pageNum, int pageSize);
	
	/**
	 * 交易歷史記錄-查詢.
	 * 
	 * @param fundTransactionVo FundTransactionVo
	 * @return 回傳保交易歷史記錄
	 */
	public List<FundTransactionVo> getFundTransactionList(FundTransactionVo fundTransactionVo);
}