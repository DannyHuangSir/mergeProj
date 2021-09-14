package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.util.List;

public class TransContactInfoVo {

	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;

	private TransContactInfoDtlVo transContactInfoDtlVo;

	private TransContactInfoOldVo transContactInfoOldVo;

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

	public TransContactInfoDtlVo getTransContactInfoDtlVo() {
		return transContactInfoDtlVo;
	}

	public void setTransContactInfoDtlVo(
			TransContactInfoDtlVo transContactInfoDtlVo) {
		this.transContactInfoDtlVo = transContactInfoDtlVo;
	}

	public TransContactInfoOldVo getTransContactInfoOldVo() {
		return transContactInfoOldVo;
	}

	public void setTransContactInfoOldVo(
			TransContactInfoOldVo transContactInfoOldVo) {
		this.transContactInfoOldVo = transContactInfoOldVo;
	}
}
