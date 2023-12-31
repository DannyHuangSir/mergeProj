package com.twfhclife.generic.api_client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.api_model.MessageTriggerRequest;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

@Service
public class MessageTemplateClient extends BaseRestClient {

	@Value("${eservice_api.message-template.trigger.url}")
	private String MSG_API_URI;//message-template/trigger
	
	public boolean sendNoticeViaMsgTemplate(String templateCode, List<String> receivers, Map<String, String> paramMap, String type) {
		MessageTriggerRequest apiReq = new MessageTriggerRequest();
		apiReq.setMessagingTemplateCode(templateCode);
		apiReq.setSendType(type);
		apiReq.setMessagingReceivers(receivers);
		apiReq.setParameters(paramMap);
		apiReq.setSystemId(ApConstants.SYSTEM_ID);
		String returnCode = this.getPostApiReturnCode(MyJacksonUtil.object2Json(apiReq), MSG_API_URI);
		if (returnCode != null) {
			return ReturnHeader.SUCCESS_CODE.equals(returnCode);
		}
		return false;
	}
	
}
