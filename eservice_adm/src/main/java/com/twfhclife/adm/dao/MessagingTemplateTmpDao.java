package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingTemplateTmpVo;

/**
 * 通信管理TMP Dao.
 * 
 * @author all
 */
public interface MessagingTemplateTmpDao {
	
	/**
	 * 通信管理TMP-分頁查詢.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳通信管理TMP-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingTemplateTmpPageList(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);

	/**
	 * 通信管理TMP-查詢結果總筆數.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳總筆數
	 */
	int getMessagingTemplateTmpPageTotal(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-查詢.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳查詢結果
	 */
	List<MessagingTemplateTmpVo> getMessagingTemplateTmp(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-新增.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingTemplateTmp(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-更新.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingTemplateTmp(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);

	/**
	 * 通信管理TMP-刪除.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingTemplateTmp(@Param("messagingTemplateTmpVo") MessagingTemplateTmpVo messagingTemplateTmpVo);
}

