package com.twfhclife.adm.domain;

import org.apache.http.Header;

public class HttpResponseVo {

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
