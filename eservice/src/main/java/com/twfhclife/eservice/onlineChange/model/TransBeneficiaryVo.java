package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransBeneficiaryVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;

	private String transBeneficiaryDtlJsonData;
	
	private List<TransBeneficiaryDtlVo> transBeneficiaryDtlList;
	
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

	public String getTransBeneficiaryDtlJsonData() {
		return transBeneficiaryDtlJsonData;
	}

	public void setTransBeneficiaryDtlJsonData(String transBeneficiaryDtlJsonData) {
		this.transBeneficiaryDtlJsonData = transBeneficiaryDtlJsonData;
	}

	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlList() {
		return transBeneficiaryDtlList;
	}

	public void setTransBeneficiaryDtlList(List<TransBeneficiaryDtlVo> transBeneficiaryDtlList) {
		this.transBeneficiaryDtlList = transBeneficiaryDtlList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}