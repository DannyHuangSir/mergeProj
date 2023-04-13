package com.twfhclife.eservice.onlineChange.model;

public class TransElectronicFormVo {
	
	private int id;
	private String transNum;
	private String eInfoN;
	private String oldEInfoN;
	private String email;
	
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
	public String geteInfoN() {
		return eInfoN;
	}
	public void seteInfoN(String eInfoN) {
		this.eInfoN = eInfoN;
	}
	public String getOldEInfoN() {
		return oldEInfoN;
	}
	public void setOldEInfoN(String oldEInfoN) {
		this.oldEInfoN = oldEInfoN;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
}
