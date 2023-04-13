package com.twfhclife.eservice.onlineChange.model;

public class ContractRevocationPDFVo {

	
	/** 要保人 */
	private String lipmName;
	/** 被保險人 */
	private String lipiName;
	/** 保單號碼 */
	private String lipmInsuNo;
	/** 主約險種 */
	private String settChName;
	/** 照會日期 */
	private String noteDate;
	/** 業務員 */
	private String name;
	/** 經攬單位 */
	private String aginInveArea;
	/** 日期 */
	private String date;
	/** 照會內容 */
	private String content;
	/** 服務人員Mail */
	private String email;
	/** UserMail */
	private String userMail;
	
	private String transNum;
	
	public String getLipmName() {
		return lipmName;
	}
	public void setLipmName(String lipmName) {
		this.lipmName = lipmName;
	}
	public String getLipiName() {
		return lipiName;
	}
	public void setLipiName(String lipiName) {
		this.lipiName = lipiName;
	}
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}

	public String getSettChName() {
		return settChName;
	}
	public void setSettChName(String settChName) {
		this.settChName = settChName;
	}
	public String getNoteDate() {
		return noteDate;
	}
	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAginInveArea() {
		return aginInveArea;
	}
	public void setAginInveArea(String aginInveArea) {
		this.aginInveArea = aginInveArea;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
		
}
