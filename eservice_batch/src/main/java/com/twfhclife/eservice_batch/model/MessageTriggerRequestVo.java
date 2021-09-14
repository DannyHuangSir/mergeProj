package com.twfhclife.eservice_batch.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class MessageTriggerRequestVo {
	
	public static final String SEND_TYPE_MAIL = "email";
	public static final String SEND_TYPE_SMS = "sms";

	/** 通信模版代碼 */
	@SerializedName("messagingTemplateCode")
	private String messagingTemplateCode;

	/** 通信模版狀態-事件類型-寄送類型 */
	@SerializedName("sendType")
	private String sendType;

	/** 通信模版狀態-事件類型-通信內容 */
	@SerializedName("parameters")
	private Map<String, String> parameters;

	/** 通信模版狀態-參數(收件人資訊) */
	@SerializedName("messagingReceivers")
	private List<String> messagingReceivers;

	private String systemId;

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

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
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
