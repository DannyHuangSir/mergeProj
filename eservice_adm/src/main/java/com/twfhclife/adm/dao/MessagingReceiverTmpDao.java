package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingReceiverTmpVo;

/**
 * 模板收件人訊息TMP表 Dao.
 * 
 * @author all
 */
public interface MessagingReceiverTmpDao {
	
	/**
	 * 模板收件人訊息TMP表-分頁查詢.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳模板收件人訊息TMP表-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingReceiverTmpPageList(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);

	/**
	 * 模板收件人訊息TMP表-查詢結果總筆數.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳總筆數
	 */
	int getMessagingReceiverTmpPageTotal(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);
	
	/**
	 * 模板收件人訊息TMP表-查詢.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳查詢結果
	 */
	List<MessagingReceiverTmpVo> getMessagingReceiverTmp(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);
	
	/**
	 * 模板收件人訊息TMP表-新增.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingReceiverTmp(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);
	
	/**
	 * 模板收件人訊息TMP表-更新.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingReceiverTmp(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);

	/**
	 * 模板收件人訊息TMP表-刪除.
	 * 
	 * @param messagingReceiverTmpVo MessagingReceiverTmpVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingReceiverTmp(@Param("messagingReceiverTmpVo") MessagingReceiverTmpVo messagingReceiverTmpVo);
}

