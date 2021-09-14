package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingParameterTmpVo;

/**
 * 模板變數TMP表 Dao.
 * 
 * @author all
 */
public interface MessagingParameterTmpDao {
	
	/**
	 * 模板變數TMP表-分頁查詢.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳模板變數TMP表-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingParameterTmpPageList(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);

	/**
	 * 模板變數TMP表-查詢結果總筆數.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳總筆數
	 */
	int getMessagingParameterTmpPageTotal(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);
	
	/**
	 * 模板變數TMP表-查詢.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳查詢結果
	 */
	List<MessagingParameterTmpVo> getMessagingParameterTmp(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);
	
	/**
	 * 模板變數TMP表-新增.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingParameterTmp(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);
	
	/**
	 * 模板變數TMP表-更新.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingParameterTmp(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);

	/**
	 * 模板變數TMP表-刪除.
	 * 
	 * @param messagingParameterTmpVo MessagingParameterTmpVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingParameterTmp(@Param("messagingParameterTmpVo") MessagingParameterTmpVo messagingParameterTmpVo);
}

