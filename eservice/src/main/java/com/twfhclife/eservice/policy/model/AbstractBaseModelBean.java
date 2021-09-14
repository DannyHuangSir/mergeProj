package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractBaseModelBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 使用者保單號碼清單 */
	private List<String> policyNos;
	
	/** 使用者證號 */
	private String rocId;
	
	public List<String> getPolicyNos() {
		return policyNos;
	}

	public void setPolicyNos(List<String> policyNos) {
		this.policyNos = policyNos;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}