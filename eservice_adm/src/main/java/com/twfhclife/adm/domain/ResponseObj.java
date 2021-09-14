package com.twfhclife.adm.domain;

public class ResponseObj {
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	
	private String result;
	private String resultMsg;
	private Object resultData;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

}
