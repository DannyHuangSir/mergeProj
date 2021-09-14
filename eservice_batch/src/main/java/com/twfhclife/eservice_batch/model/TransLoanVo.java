package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransLoanVo {

	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private BigDecimal loanAmount;

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

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public TransBankInfoVo getTransBankInfoVo() {
		return transBankInfoVo;
	}

	public void setTransBankInfoVo(TransBankInfoVo transBankInfoVo) {
		this.transBankInfoVo = transBankInfoVo;
	}
}