package com.twfhclife.adm.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.MessagingTemplateVo;

/**
 * 通信管理 Dao.
 * 
 * @author all
 */
public interface MessagingTemplateDao {
	
	/**
	 * 通信管理-分頁查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳通信管理-分頁查詢結果
	 */
	List<Map<String, Object>> getMessagingTemplatePageList(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);

	/**
	 * 通信管理-查詢結果總筆數.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳總筆數
	 */
	int getMessagingTemplatePageTotal(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳查詢結果
	 */
	List<MessagingTemplateVo> getMessagingTemplate(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-新增.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	int insertMessagingTemplate(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);
	
	/**
	 * 通信管理-更新.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	int updateMessagingTemplate(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);

	/**
	 * 通信管理-刪除.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	int deleteMessagingTemplate(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);

	/**
	 * 通信管理-檢查模板代碼.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	String checkMessagingCode(@Param("messagingTemplateVo") MessagingTemplateVo messagingTemplateVo);
	
	MessagingTemplateVo checkReviewStatus(@Param("messagingTemplateId") BigDecimal messagingTemplateId);
	
	BigDecimal getMessagingTemplateId();
	
	BigDecimal getMessagingReceiverId();
}
