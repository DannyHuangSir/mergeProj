package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.LoginLogVo;

/**
 * 登入記錄 Dao.
 * 
 * @author all
 */
public interface LoginLogDao {
	
	/**
	 * 取得登入記錄查詢結果
	 * 
	 * @param vo LoginLogVo
	 * @return 回傳登入記錄查詢
	 */
	public List<Map<String, Object>> getLoginRecordDetail(@Param("loginLogVo") LoginLogVo vo);
	
	/**
	 * 取得登入記錄查詢結果總筆數
	 * 
	 * @param vo LoginLogVo
	 * @return 回傳總筆數
	 */
	public int getLoginRecordDetailTotal(@Param("loginLogVo") LoginLogVo vo);
	
	/**
	 * 新增登入記錄.
	 * 
	 * @param vo LoginLogVo
	 */
	public void addLoginLog(@Param("loginLogVo") LoginLogVo vo);

	/**
	 * 寫登出記錄.
	 * 
	 * @param vo LoginLogVo
	 */
	public void updateLogoutLog(@Param("loginLogVo") LoginLogVo vo);
}
