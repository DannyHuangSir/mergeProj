package com.twfhclife.generic.dao.adm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.adm.domain.MessagingTemplateReqVo;
import com.twfhclife.eservice.api.adm.model.MessagingTemplateVo;


/**
 * 參數維護 Dao.
 * 
 * @author all
 */
public interface MessageTemplateDao {
	
	/**
	 * 通信管理-查詢通信主檔By Code
	 * 
	 * @param messagingTemplateCode
	 * @return MessagingTemplateVo
	 */
	MessagingTemplateVo getMessageTemplateByCode(@Param("messagingTemplateCode") String messagingTemplateCode);

	List<String> getReceiversByTemplateId(@Param("messagingTemplateId") int messagingTemplateId, @Param("receiverType") String receiverType);
	
	List<Map<String, Object>> getMessagingTemplatePageList(@Param("messagingTemplateReqVo") MessagingTemplateReqVo messagingTemplateReqVo);
	
	int getMessagingTemplatePageTotal(@Param("messagingTemplateVo") MessagingTemplateReqVo messagingTemplateVo);
}
