package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.MessagingTemplateVo;

/**
 * 通信管理服務.
 * 
 * @author all
 */
public interface IMessagingTemplateService {
	
	/**
	 * 通信管理-分頁查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳通信管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getMessagingTemplatePageList(MessagingTemplateVo messagingTemplateVo);

	/**
	 * 通信管理-查詢結果總筆數.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳總筆數
	 */
	public int getMessagingTemplatePageTotal(MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳查詢結果
	 */
	List<MessagingTemplateVo> getMessagingTemplate(MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-新增.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	public int insertMessagingTemplate(MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-更新.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	public int updateMessagingTemplate(MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-停用.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	public int stopMessagingTemplate(MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-更新狀態.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	public String updateMessagingTemplateStatus(MessagingTemplateVo messagingTemplateVo);

	/**
	 * 通信管理-檢查模板代碼.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	public boolean checkMessagingCode(MessagingTemplateVo messagingTemplateVo);
}
