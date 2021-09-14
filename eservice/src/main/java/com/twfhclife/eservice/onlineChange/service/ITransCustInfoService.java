package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCustInfoVo;

/**
 * 變更基本資料服務.
 * 
 * @author all
 */
public interface ITransCustInfoService {
	
	/**
	 * 變更基本資料-查詢.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳查詢結果
	 */
	public List<TransCustInfoVo> getTransCustInfo(TransCustInfoVo transCustInfoVo);
	
	/**
	 * 變更基本資料-新增.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳影響筆數
	 */
	public int insertTransCustInfo(TransCustInfoVo transCustInfoVo);
	
	/**
	 * 變更基本資料-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransCustInfoVo getTransCustInfoDetail(String transNum);
}