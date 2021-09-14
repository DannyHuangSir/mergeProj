package com.twfhclife.adm.model;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessagingReceiverTmpVo {
	
	/**  */
	private BigDecimal messagingReceiverId;
	/**  */
	private String messagingReceiverType;
	/**  */
	private String messagingReceiverValue;
	/**  */
	private BigDecimal messagingTemplateId;
	
	public BigDecimal getMessagingReceiverId() {
		return this.messagingReceiverId;
	}
	
	public void setMessagingReceiverId(BigDecimal messagingReceiverId) {
		this.messagingReceiverId = messagingReceiverId;
	}
	
	public String getMessagingReceiverType() {
		return this.messagingReceiverType;
	}
	
	public void setMessagingReceiverType(String messagingReceiverType) {
		this.messagingReceiverType = messagingReceiverType;
	}
	
	public String getMessagingReceiverValue() {
		return this.messagingReceiverValue;
	}
	
	public void setMessagingReceiverValue(String messagingReceiverValue) {
		this.messagingReceiverValue = messagingReceiverValue;
	}
	
	public BigDecimal getMessagingTemplateId() {
		return this.messagingTemplateId;
	}
	
	public void setMessagingTemplateId(BigDecimal messagingTemplateId) {
		this.messagingTemplateId = messagingTemplateId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

