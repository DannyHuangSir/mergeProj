package com.twfhclife.eservice.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class FunctionDivVo {

	/**  */
	private Integer divId;
	/**  */
	private String divName;
	/**  */
	private BigDecimal functionId;

	public Integer getDivId() {
		return divId;
	}

	public void setDivId(Integer divId) {
		this.divId = divId;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public BigDecimal getFunctionId() {
		return functionId;
	}

	public void setFunctionId(BigDecimal functionId) {
		this.functionId = functionId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}