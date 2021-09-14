package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransDetailVo;

/**
 * 線上申請明細服務.
 * 
 * @author all
 */
public interface ITransHistoryDetailService {

	/**
	 * 查詢線上申請紀錄明細.
	 * 
	 * @param transNumList 交易序號清單
	 * @return 回傳申請明細
	 */
	public List<TransDetailVo> getTransHistoryDetailList(List<String> transNumList);
}