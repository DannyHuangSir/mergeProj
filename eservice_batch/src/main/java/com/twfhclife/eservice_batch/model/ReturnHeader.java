package com.twfhclife.eservice_batch.model;

public class ReturnHeader {
	private String returnCode;
	private String returnMesg;
	private String processId;
	private String status;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMesg() {
		return returnMesg;
	}
	public void setReturnMesg(String returnMesg) {
		this.returnMesg = returnMesg;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
