package com.twfhclife.jd.api_model;

import java.io.Serializable;

public class ApiResponseObj<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";

	private ReturnHeader returnHeader;
	private T result;

	public ReturnHeader getReturnHeader() {
		return returnHeader;
	}

	public void setReturnHeader(ReturnHeader returnHeader) {
		this.returnHeader = returnHeader;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
