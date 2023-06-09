package com.twfhclife.eservice_batch.model;

public class TransDeratePaidOffVo {

	private int id;
	private String transNum;
	private String derateAmt;
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
	public String getDerateAmt() {
		return derateAmt;
	}
	public void setDerateAmt(String derateAmt) {
		this.derateAmt = derateAmt;
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
