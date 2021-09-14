package com.twfhclife.eservice.policy.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InvestmentProductVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 保險商品代碼 */
	private String prodNo;
	
	/** 商品名稱 */
	private String name;
	
	/** 商品幣別代碼 */
	private String currCode;
	
	/** 商品幣別名稱 */
	private String currName;
	
	/** 生效日期 */
	private String effectDate;
	
	/** 狀況代碼 */
	private String stCode;
	
	public String getProdNo() {
		return prodNo;
	}

	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public String getCurrName() {
		return currName;
	}

	public void setCurrName(String currName) {
		this.currName = currName;
	}

	public String getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}

	public String getStCode() {
		return stCode;
	}

	public void setStCode(String stCode) {
		this.stCode = stCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}