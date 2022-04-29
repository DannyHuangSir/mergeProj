package com.twfhclife.alliance.model;

public class MedicalTreatmentClaimToFromDataVo {

	/**
	 * 轉收公司代號
	 */
	private String to;
	
	/**
	 * 首家公司代號
	 */
	private String from;
	
	/**
	 * 案件狀態
	 */
	private String status;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
