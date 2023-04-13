package com.twfhclife.eservice.api.elife.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Csp001Detail {
	
	@JsonProperty(value = "CB-SCREEN-NAME")
	private String cbScreenName;
	@JsonProperty(value = "CB-FUNC-CODE")
	private String cbFuncCode;
	@JsonProperty(value = "DS16-GP-NO")
	private String ds16GpNo;
	@JsonProperty(value = "D16-CHAIN-PAID-UP")
	private String d16ChainPaidUp;
	@JsonProperty(value = "D16-CHAN-AMT")
	private String d16ChanAmt;
	@JsonProperty(value = "DS16-EXTEND-TERM-DATE")
	private String ds16ExtendTermDate;
	@JsonProperty(value = "DS16-NEW-AMT")
	private String ds16NewAmt;
	@JsonProperty(value = "DS16-DSEC")
	private String ds16Dsec;
	public String getCbScreenName() {
		return cbScreenName;
	}
	public void setCbScreenName(String cbScreenName) {
		this.cbScreenName = cbScreenName;
	}
	public String getCbFuncCode() {
		return cbFuncCode;
	}
	public void setCbFuncCode(String cbFuncCode) {
		this.cbFuncCode = cbFuncCode;
	}
	public String getDs16GpNo() {
		return ds16GpNo;
	}
	public void setDs16GpNo(String ds16GpNo) {
		this.ds16GpNo = ds16GpNo;
	}
	public String getD16ChainPaidUp() {
		return d16ChainPaidUp;
	}
	public void setD16ChainPaidUp(String d16ChainPaidUp) {
		this.d16ChainPaidUp = d16ChainPaidUp;
	}
	public String getD16ChanAmt() {
		return d16ChanAmt;
	}
	public void setD16ChanAmt(String d16ChanAmt) {
		this.d16ChanAmt = d16ChanAmt;
	}
	public String getDs16ExtendTermDate() {
		return ds16ExtendTermDate;
	}
	public void setDs16ExtendTermDate(String ds16ExtendTermDate) {
		this.ds16ExtendTermDate = ds16ExtendTermDate;
	}
	public String getDs16NewAmt() {
		return ds16NewAmt;
	}
	public void setDs16NewAmt(String ds16NewAmt) {
		this.ds16NewAmt = ds16NewAmt;
	}
	public String getDs16Dsec() {
		return ds16Dsec;
	}
	public void setDs16Dsec(String ds16Dsec) {
		this.ds16Dsec = ds16Dsec;
	}
		
}
