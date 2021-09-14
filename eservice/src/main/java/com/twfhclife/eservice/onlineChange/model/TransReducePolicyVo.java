package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransReducePolicyVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;

	private String transReducePolicyDtlJsonData;
	
	private List<TransReducePolicyDtlVo> transReducePolicyDtlList;
	
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

	public String getTransReducePolicyDtlJsonData() {
		return transReducePolicyDtlJsonData;
	}

	public void setTransReducePolicyDtlJsonData(String transReducePolicyDtlJsonData) {
		this.transReducePolicyDtlJsonData = transReducePolicyDtlJsonData;
	}

	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlList() {
		return transReducePolicyDtlList;
	}

	public void setTransReducePolicyDtlList(List<TransReducePolicyDtlVo> transReducePolicyDtlList) {
		this.transReducePolicyDtlList = transReducePolicyDtlList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}