package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransChangeAccountVo {

	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String changeType;
	/**  */
	private String acctType;

	private TransBankInfoVo transBankInfoVo;

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTransNum() {
		return this.transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getAcctType() {
		return this.acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public TransBankInfoVo getTransBankInfoVo() {
		return transBankInfoVo;
	}

	public void setTransBankInfoVo(TransBankInfoVo transBankInfoVo) {
		this.transBankInfoVo = transBankInfoVo;
	}
}