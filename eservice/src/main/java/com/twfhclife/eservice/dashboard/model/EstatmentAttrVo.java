package com.twfhclife.eservice.dashboard.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EstatmentAttrVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**  */
	private BigDecimal id;
	/**  */
	private BigDecimal estatmentId;
	/**  */
	private String estatmentKey;
	/**  */
	private String estatmentValue;
	
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public BigDecimal getEstatmentId() {
		return this.estatmentId;
	}
	
	public void setEstatmentId(BigDecimal estatmentId) {
		this.estatmentId = estatmentId;
	}
	
	public String getEstatmentKey() {
		return this.estatmentKey;
	}
	
	public void setEstatmentKey(String estatmentKey) {
		this.estatmentKey = estatmentKey;
	}
	
	public String getEstatmentValue() {
		return this.estatmentValue;
	}
	
	public void setEstatmentValue(String estatmentValue) {
		this.estatmentValue = estatmentValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

