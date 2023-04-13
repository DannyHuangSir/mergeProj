package com.twfhclife.generic.api_model;

import java.io.Serializable;

public class TransCtcLilipmRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String lipmId;
		
	private String policyNo;
	
	private String lipmInsuSeqNo;	

	public String getLipmId() {
		return lipmId;
	}

	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getLipmInsuSeqNo() {
		return lipmInsuSeqNo;
	}

	public void setLipmInsuSeqNo(String lipmInsuSeqNo) {
		this.lipmInsuSeqNo = lipmInsuSeqNo;
	}
	

}
