package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractOnlineChangeModelBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<String> policyNoList;
	
	@JsonIgnore
	private String authenticationNum;
	
	@JsonIgnore
	private String userId;
	
	public List<String> getPolicyNoList() {
		return policyNoList;
	}

	public void setPolicyNoList(List<String> policyNoList) {
		this.policyNoList = policyNoList;
	}

	public String getAuthenticationNum() {
		return authenticationNum;
	}

	public void setAuthenticationNum(String authenticationNum) {
		this.authenticationNum = authenticationNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}