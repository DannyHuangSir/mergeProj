package com.twfhclife.eservice.dashboard.service;

import java.util.List;

import com.twfhclife.eservice.web.model.TransVo;

/**
 * 線上申請進度服務.
 * 
 * @author alan
 */
public interface IApplyProgressService {

	/**
	 * 取得使用者的線上申請記錄-申請中
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的線上申請記錄
	 */
	public List<TransVo> getApplyProgressList(String rocId);

	/**
	 * 取得使用者的線上申請記錄.
	 * 
	 * @param rocId 用戶證號
	 * @param transStatus 申請狀態
	 * @return 回傳使用者的線上申請記錄
	 */
	public List<TransVo> getChangeHistoryList(String rocId, String transStatus);
}
