package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingReceiverVo;

/**
 * 模板收件人訊息主表 Dao.
 * 
 * @author all
 */
public interface MessagingReceiverDao {
	
	/**
	 * 模板收件人訊息主表-分頁查詢.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳模板收件人訊息主表-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingReceiverPageList(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);

	/**
	 * 模板收件人訊息主表-查詢結果總筆數.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳總筆數
	 */
	int getMessagingReceiverPageTotal(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);
	
	/**
	 * 模板收件人訊息主表-查詢.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳查詢結果
	 */
	List<MessagingReceiverVo> getMessagingReceiver(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);
	
	/**
	 * 模板收件人訊息主表-新增.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingReceiver(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);
	
	/**
	 * 模板收件人訊息主表-更新.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingReceiver(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);

	/**
	 * 模板收件人訊息主表-刪除.
	 * 
	 * @param messagingReceiverVo MessagingReceiverVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingReceiver(@Param("messagingReceiverVo") MessagingReceiverVo messagingReceiverVo);
}

