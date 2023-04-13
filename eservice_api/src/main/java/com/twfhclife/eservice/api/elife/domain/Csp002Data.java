package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Csp002Data {
	@JsonProperty(value = "values")
	private List<Csp002Detail> values;
	
	private String token;
	@JsonProperty(value = "detail_status")
	private String detailStatus ;
	@JsonProperty(value = "detail_message")
	private String detailMessage;
	public List<Csp002Detail> getValues() {
		return values;
	}
	public void setValues(List<Csp002Detail> values) {
		this.values = values;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDetailStatus() {
		return detailStatus;
	}
	public void setDetailStatus(String detailStatus) {
		this.detailStatus = detailStatus;
	}
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
	
	
}
