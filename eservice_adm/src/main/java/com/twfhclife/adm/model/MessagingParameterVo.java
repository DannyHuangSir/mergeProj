package com.twfhclife.adm.model;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessagingParameterVo {
	
	/**  */
	private BigDecimal messagingTemplateId;
	/**  */
	private BigDecimal parameterId;
	
	private String parameterValue;
	
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

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

