package com.twfhclife.eservice_api.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.adm.domain.MessagingTemplateReqVo;

public interface IMessagingTemplateService {
	
	public String triggerMessageTemplate(MessageTriggerRequestVo requestVo);
	
	public List<Map<String, Object>> getMessagingTemplatePageList(MessagingTemplateReqVo messagingTemplateVo);

	int getMessagingTemplatePageTotal(MessagingTemplateReqVo messagingTemplateVo);
	
}
