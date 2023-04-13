package com.twfhclife.generic.api_model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Csp001Data {	
	@JsonProperty(value = "values")
	private List<Csp001Detail> values;
	
	private String token;
	@JsonProperty(value = "detail_status")
	private String detailStatus ;
	@JsonProperty(value = "detail_message")
	private String detailMessage;
	
	public List<Csp001Detail> getValues() {
		return values;
	}
	public void setValues(List<Csp001Detail> values) {
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
