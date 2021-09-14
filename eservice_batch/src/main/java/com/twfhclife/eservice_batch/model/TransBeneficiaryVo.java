package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.util.List;

public class TransBeneficiaryVo {

	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;

	private List<TransBeneficiaryDtlVo> dtlList;

	private List<TransBeneficiaryOldVo> oldList;

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

	public List<TransBeneficiaryDtlVo> getDtlList() {
		return dtlList;
	}

	public void setDtlList(List<TransBeneficiaryDtlVo> dtlList) {
		this.dtlList = dtlList;
	}

	public List<TransBeneficiaryOldVo> getOldList() {
		return oldList;
	}

	public void setOldList(List<TransBeneficiaryOldVo> oldList) {
		this.oldList = oldList;
	}
}
