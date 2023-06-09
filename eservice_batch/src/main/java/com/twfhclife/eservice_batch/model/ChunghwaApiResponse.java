package com.twfhclife.eservice_batch.model;

public class ChunghwaApiResponse {
	
	private ReturnHeader returnHeader;
	
	private ChunghwaApiResult result ;

	public ReturnHeader getReturnHeader() {
		return returnHeader;
	}

	public void setReturnHeader(ReturnHeader returnHeader) {
		this.returnHeader = returnHeader;
	}

	public ChunghwaApiResult getResult() {
		return result;
	}

	public void setResult(ChunghwaApiResult result) {
		this.result = result;
	}
	
}
