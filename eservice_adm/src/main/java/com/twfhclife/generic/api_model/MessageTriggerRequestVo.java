package com.twfhclife.generic.api_model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessageTriggerRequestVo {

	private String systemId;
	
	/** 通信模版代碼 */
	private String messagingTemplateCode;
	
	/** 通信模版名稱 */
	private String messagingTemplateName;
	
	/** 通信模版狀態-事件類型-寄送類型 */
	private String sendType;
	
	/** 通信模版狀態-事件類型-收件者類型 */
	private String receiverMode;
	
	/** 通信模版狀態-事件類型-通信主旨 */
	private String messagingSubject;
	
	/** 通信模版狀態-事件類型-通信內容 */
	private String messagingContent;
	
	/** 通信模版狀態-事件類型-通信內容 */
	private Map<String, String> parameters;
	
	/** 通信模版狀態-參數(收件人資訊) */
	private List<String> messagingReceivers;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getMessagingTemplateCode() {
		return messagingTemplateCode;
	}

	public void setMessagingTemplateCode(String messagingTemplateCode) {
		this.messagingTemplateCode = messagingTemplateCode;
	}

	public String getMessagingTemplateName() {
		return messagingTemplateName;
	}

	public void setMessagingTemplateName(String messagingTemplateName) {
		this.messagingTemplateName = messagingTemplateName;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getReceiverMode() {
		return receiverMode;
	}

	public void setReceiverMode(String receiverMode) {
		this.receiverMode = receiverMode;
	}

	public String getMessagingSubject() {
		return messagingSubject;
	}

	public void setMessagingSubject(String messagingSubject) {
		this.messagingSubject = messagingSubject;
	}

	public String getMessagingContent() {
		return messagingContent;
	}

	public void setMessagingContent(String messagingContent) {
		this.messagingContent = messagingContent;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public List<String> getMessagingReceivers() {
		return messagingReceivers;
	}

	public void setMessagingReceivers(List<String> messagingReceivers) {
		this.messagingReceivers = messagingReceivers;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
