package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.FundPrdtVo;

/**
 * 帳戶價值交易明細服務.
 * 
 * @author all
 */
public interface IFundPrdtService {
	
	/**
	 * 帳戶價值交易明細-查詢.
	 * 
	 * @param fundPrdtVo FundPrdtVo
	 * @return 回傳查詢結果
	 */
	public List<FundPrdtVo> getFundPrdt(FundPrdtVo fundPrdtVo);

	/**
	 * 取得保單的保費費用記錄(分頁).
	 * 
	 * @param policyNo 保單號碼
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保單的保費費用記錄
	 */
	public List<FundPrdtVo> getFundPrdtPageList(String policyNo, String startDate, String endDate,
			int pageNum, int pageSize);
}