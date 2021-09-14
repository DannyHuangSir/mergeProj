package com.twfhclife.eservice.web.domain;

import java.io.Serializable;

import org.apache.http.Header;

public class HttpResponseVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int statusCode;
	private Header[] header;
	private String responseBody;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Header[] getHeader() {
		return header;
	}

	public void setHeader(Header[] header) {
		this.header = header;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

}
