package com.twfhclife.alliance.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

public class BaseResponseObj implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODE_SUCCESS = "0";
	
	public static final String CODE_ERROR   = "1";
	
	public static final String MSG_SUCCESS  = "SUCCESS";
	
	public static final String MSG_ERROR    = "error";
	
	/**
	 * 未定義錯誤001
	 */
	public static final String MSG_ERROR_S001 = "S-001";
	
	/**
	 * 代碼
	 */
	@NotEmpty(message = "code cannot empty!")
	private String code;
	
	/**
	 * 訊息
	 */
	@NotEmpty(message = "msg cannot empty!")
	private String msg;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
