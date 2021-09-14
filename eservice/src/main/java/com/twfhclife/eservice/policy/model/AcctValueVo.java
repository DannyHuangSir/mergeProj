package com.twfhclife.eservice.policy.model;

import java.io.Serializable;

/**
 * 帳戶價值物件.
 * 
 * @author alan
 */
public class AcctValueVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;
	
	/** yyy01~04 */
	private String quaterCode;
	
	/** 民國yyy年第x季 */
	private String quaterName;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getQuaterCode() {
		return quaterCode;
	}

	public void setQuaterCode(String quaterCode) {
		this.quaterCode = quaterCode;
	}

	public String getQuaterName() {
		return quaterName;
	}

	public void setQuaterName(String quaterName) {
		this.quaterName = quaterName;
	}
}