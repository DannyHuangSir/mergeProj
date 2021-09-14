package com.twfhclife.adm.service;

import java.util.List;

import com.twfhclife.adm.model.MessagingTemplateTmpVo;

/**
 * 通信管理TMP服務.
 * 
 * @author all
 */
public interface IMessagingTemplateTmpService {
	
	/**
	 * 通信管理TMP-查詢.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳查詢結果
	 */
	List<MessagingTemplateTmpVo> getMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-新增.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	public int insertMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-更新.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	public int updateMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo);
	
	/**
	 * 通信管理TMP-刪除.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	public int deleteMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo);
}