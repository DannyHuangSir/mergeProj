package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.CommLogVo;


/**
 * 通知紀錄DAO
 *
 */
public interface CommLogDao {

	/**
	 * 取得通知紀錄查詢結果
	 * @param vo
	 * @return List
	 */
	public List<Map<String, Object>> getCommLogDetail(@Param("commLogVo") CommLogVo vo);
	
	/**
	 * 取得通知紀錄查詢結果總筆數
	 * @param vo
	 * @return int
	 */
	public int getCommLogDetailPageTotal(@Param("commLogVo") CommLogVo vo);
	
	/**
	 * 取得所有發送發型
	 * @return List
	 */
	public List<String> getAllCommType();
	
	/**
	 * 取得指定COMM_CNT
	 * @param vo
	 * @return List<String>
	 */
	public List<String> getCommCntById(@Param("commLogVo") CommLogVo vo);
	
}
