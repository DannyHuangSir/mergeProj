package com.twfhclife.alliance.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseRequestVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 此案件編號為新案件，
	 */
	public static final String TYPE_NEW = "1";
	
	/**
	 * 此案件編號於首家已收到紙本
	 */
	public static final String TYPE_GOT_PAPER = "2";
	
	/**
	 * 案件編號
	 */
	private String caseId;
	
	/**
	 * 保險理賠：通知的類型</b>
	 * 死亡除戶：回報類別
	 */
	private String type;

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
