package com.twfhclife.eservice.api.adm.model;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessagingParameterTmpVo {
	
	/**  */
	private BigDecimal messagingTemplateId;
	/**  */
	private BigDecimal parameterId;
	
	public BigDecimal getMessagingTemplateId() {
		return this.messagingTemplateId;
	}
	
	public void setMessagingTemplateId(BigDecimal messagingTemplateId) {
		this.messagingTemplateId = messagingTemplateId;
	}
	
	public BigDecimal getParameterId() {
		return this.parameterId;
	}
	
	public void setParameterId(BigDecimal parameterId) {
		this.parameterId = parameterId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

