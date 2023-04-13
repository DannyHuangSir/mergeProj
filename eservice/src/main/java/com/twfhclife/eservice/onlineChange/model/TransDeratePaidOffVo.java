package com.twfhclife.eservice.onlineChange.model;

public class TransDeratePaidOffVo extends AbstractOnlineChangeModelBean{
	private static final long serialVersionUID = 1L;
	/** 保單號碼 */
	private String policyNo;
	/** 投保日期, */
	private String iinsuBegDate;
	/** 最近繳費日 */
	private String tredRcpDate;
	/** 繳別 */
	private String rcpCode;
	/** 最近已繳費期次 */
	private String tredPaabDate;
	/** 主契約保額 */
	private String mainAmt;
	/** 下期應繳期次 */
	private String nextTredPaabDate;

	private int id;
	private String transNum;
	/** 減額繳清保額 */
	private String derateAmt;
	private String email;
	
	public String getIinsuBegDate() {
		return iinsuBegDate;
	}
	public void setIinsuBegDate(String iinsuBegDate) {
		this.iinsuBegDate = iinsuBegDate;
	}
	public String getTredRcpDate() {
		return tredRcpDate;
	}
	public void setTredRcpDate(String tredRcpDate) {
		this.tredRcpDate = tredRcpDate;
	}
	public String getRcpCode() {
		return rcpCode;
	}
	public void setRcpCode(String rcpCode) {
		this.rcpCode = rcpCode;
	}
	public String getTredPaabDate() {
		return tredPaabDate;
	}
	public void setTredPaabDate(String tredPaabDate) {
		this.tredPaabDate = tredPaabDate;
	}
	public String getMainAmt() {
		return mainAmt;
	}
	public void setMainAmt(String mainAmt) {
		this.mainAmt = mainAmt;
	}
	public String getNextTredPaabDate() {
		return nextTredPaabDate;
	}
	public void setNextTredPaabDate(String nextTredPaabDate) {
		this.nextTredPaabDate = nextTredPaabDate;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
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
	
}
