package com.twfhclife.eservice.onlineChange.model;

public class TransRenewReduceVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;
	/**  */
	private String transNum;
	/**  */
	private String transType;
	
	public String getTransNum() {
		return transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
}