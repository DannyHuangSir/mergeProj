package com.twfhclife.eservice.api.elife.domain;

import java.io.Serializable;
import java.util.List;

public class TransAddResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<String> transNums;
	
	private String transResult;
	
	private String transResultMsg;
	
	public List<String> getTransNums() {
		return transNums;
	}

	public void setTransNums(List<String> transNums) {
		this.transNums = transNums;
	}

	public String getTransResult() {
		return transResult;
	}

	public void setTransResult(String transResult) {
		this.transResult = transResult;
	}

	public String getTransResultMsg() {
		return transResultMsg;
	}

	public void setTransResultMsg(String transResultMsg) {
		this.transResultMsg = transResultMsg;
	}
}