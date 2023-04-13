package com.twfhclife.eservice.api.elife.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Csp002Detail {
	@JsonProperty(value = "FSZ2-SCN-NAME")
	private String fsz2ScnName;
	@JsonProperty(value = "FSZ2-FUNC-CODE")
	private String fsz2FuncCode;;
	@JsonProperty(value = "FSZ2-INSU-NO")
	private String fsz2InsuNo;
	@JsonProperty(value = "FSZ2-CALC-DATE")
	private String fsz2CalcDate;
	@JsonProperty(value = "FSZ2-CALC-TYPE")
	private String fsz2CalcType;
	@JsonProperty(value = "FSZ2-AMT")
	private String fsz2Amt;
	public String getFsz2ScnName() {
		return fsz2ScnName;
	}
	public void setFsz2ScnName(String fsz2ScnName) {
		this.fsz2ScnName = fsz2ScnName;
	}
	public String getFsz2FuncCode() {
		return fsz2FuncCode;
	}
	public void setFsz2FuncCode(String fsz2FuncCode) {
		this.fsz2FuncCode = fsz2FuncCode;
	}
	public String getFsz2InsuNo() {
		return fsz2InsuNo;
	}
	public void setFsz2InsuNo(String fsz2InsuNo) {
		this.fsz2InsuNo = fsz2InsuNo;
	}
	public String getFsz2CalcDate() {
		return fsz2CalcDate;
	}
	public void setFsz2CalcDate(String fsz2CalcDate) {
		this.fsz2CalcDate = fsz2CalcDate;
	}
	public String getFsz2CalcType() {
		return fsz2CalcType;
	}
	public void setFsz2CalcType(String fsz2CalcType) {
		this.fsz2CalcType = fsz2CalcType;
	}
	public String getFsz2Amt() {
		return fsz2Amt;
	}
	public void setFsz2Amt(String fsz2Amt) {
		this.fsz2Amt = fsz2Amt;
	}
}
