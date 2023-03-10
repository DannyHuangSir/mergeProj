package com.twfhclife.eservice.web.domain;

import java.io.Serializable;

public class PortfolioResponseObj implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String ACCESS_DENY = "ACCESS_DENY";

	private String result;
	private String resultMsg;
	private Object resultData;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	private String endDate;

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
