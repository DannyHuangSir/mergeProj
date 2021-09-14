package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class TransResendPolicyVo {
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	/**  */
	private String name;
	/**  */
	private String tel;
	/**  */
	private String address;
	
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
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTel() {
		return this.tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}