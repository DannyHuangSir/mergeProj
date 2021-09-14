package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.LoginLogVo;

/**
 * 報表查詢-登入記錄查詢服務.
 * 
 * @author all
 */
public interface ILoginRecordService {
	
	/**
	 * 取得登入記錄查詢結果
	 * 
	 * @param vo AuditLogVo
	 * @return 回傳登入記錄查詢
	 */
	public List<Map<String, Object>> getLoginRecordDetail(LoginLogVo vo);
	
	/**
	 * 取得登入記錄查詢結果總筆數
	 * 
	 * @param vo AuditLogVo
	 * @return 回傳總筆數
	 */
	public int getLoginRecordDetailTotal(LoginLogVo vo);

}
