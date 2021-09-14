package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.CommLogVo;

/**
 * 
 * 報表查詢-通知記錄查詢服務
 *
 */
public interface ICommLogService {

	/**
	 * 取得通知記錄查詢結果
	 * @param vo
	 * @return List
	 */
	public List<Map<String, Object>> getCommLogDetail(CommLogVo vo);
	
	/**
	 * 取得通知紀錄查詢結果總筆數
	 * @param vo
	 * @return int
	 */
	public int getCommLogDetailPageTotal(CommLogVo vo);
	
	/**
	 * 取得所有發送類型
	 * @return List
	 */
	public List<String> getAllCommType();
	
	/**
	 * 取得指定COMM_CNT
	 * @param id
	 * @return List
	 */
	public List<String> getCommCntById(String id);
	
	
}
