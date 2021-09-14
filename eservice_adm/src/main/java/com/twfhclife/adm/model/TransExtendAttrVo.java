package com.twfhclife.adm.model;

import java.math.BigDecimal;

public class TransExtendAttrVo {

	private BigDecimal id;
	private String transNum;
	private String attrKey;
	private String attrValue;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

}
