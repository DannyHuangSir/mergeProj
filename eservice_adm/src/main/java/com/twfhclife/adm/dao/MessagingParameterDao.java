package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingParameterVo;

/**
 * 模板變數主表 Dao.
 * 
 * @author all
 */
public interface MessagingParameterDao {
	
	/**
	 * 模板變數主表-分頁查詢.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳模板變數主表-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingParameterPageList(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);

	/**
	 * 模板變數主表-查詢結果總筆數.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳總筆數
	 */
	int getMessagingParameterPageTotal(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);
	
	/**
	 * 模板變數主表-查詢.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳查詢結果
	 */
	List<MessagingParameterVo> getMessagingParameter(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);
	
	/**
	 * 模板變數主表-新增.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingParameter(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);
	
	/**
	 * 模板變數主表-更新.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingParameter(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);

	/**
	 * 模板變數主表-刪除.
	 * 
	 * @param messagingParameterVo MessagingParameterVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingParameter(@Param("messagingParameterVo") MessagingParameterVo messagingParameterVo);
}

