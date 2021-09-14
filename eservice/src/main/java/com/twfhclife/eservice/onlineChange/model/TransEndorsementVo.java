package com.twfhclife.eservice.onlineChange.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransEndorsementVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private String transNum;
	/**  */
	private String textContent;
	/**  */
	private String textOrder;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getTextOrder() {
		return textOrder;
	}

	public void setTextOrder(String textOrder) {
		this.textOrder = textOrder;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}