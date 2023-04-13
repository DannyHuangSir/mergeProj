package com.twfhclife.generic.api_model;

import java.io.Serializable;

public class TransCtcLipmdaRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
