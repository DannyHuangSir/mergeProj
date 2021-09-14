package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class VerifyPolicyResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String verifyResult;
	
	private String verifyMsg;
	
	private List<String> optiionList;//年金給付方式

	public String getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}

	public String getVerifyMsg() {
		return verifyMsg;
	}

	public void setVerifyMsg(String verifyMsg) {
		this.verifyMsg = verifyMsg;
	}

	public List<String> getOptiionList() {
		return optiionList;
	}

	public void setOptiionList(List<String> optiionList) {
		this.optiionList = optiionList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}