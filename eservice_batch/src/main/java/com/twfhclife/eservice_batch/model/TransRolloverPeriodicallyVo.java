package com.twfhclife.eservice_batch.model;

public class TransRolloverPeriodicallyVo {
	
	private int id;
	private String transNum;
	private String rolloverAmt;
	private String rolloverDate;
	private String email;
	private String nextTredPaabDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	public String getRolloverAmt() {
		return rolloverAmt;
	}
	public void setRolloverAmt(String rolloverAmt) {
		this.rolloverAmt = rolloverAmt;
	}
	public String getRolloverDate() {
		return rolloverDate;
	}
	public void setRolloverDate(String rolloverDate) {
		this.rolloverDate = rolloverDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNextTredPaabDate() {
		return nextTredPaabDate;
	}
	public void setNextTredPaabDate(String nextTredPaabDate) {
		this.nextTredPaabDate = nextTredPaabDate;
	}

}
