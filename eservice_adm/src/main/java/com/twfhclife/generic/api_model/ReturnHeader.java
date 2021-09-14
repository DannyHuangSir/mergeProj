package com.twfhclife.generic.api_model;

public class ReturnHeader {
	
	public static final String ERROR_CODE = "ERROR";
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String FAIL_CODE = "FAIL";
	public static final String NODATA_CODE = "NO_DATA";
	
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

	public void setReturnHeader(String returnCode, String returnMesg, String processId, String status) {
		this.setReturnCode(returnCode);
		this.setReturnMesg(returnMesg);
		this.setProcessId(processId);
		this.setStatus(status);
	}
	
	public void setSuccessReturnHeader(String processId, String status) {
		this.setReturnCode(ERROR_CODE);
		this.setReturnMesg("");
		this.setProcessId(processId);
		this.setStatus(status);
	}
	
	public void setErrorReturnHeader(String returnMesg, String processId, String status) {
		this.setReturnCode(ERROR_CODE);
		this.setReturnMesg(returnMesg);
		this.setProcessId(processId);
		this.setStatus(status);
	}
}
