package com.twfhclife.eservice.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransPolicyVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String transNum;
	/**  */
	private String policyNo;
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

