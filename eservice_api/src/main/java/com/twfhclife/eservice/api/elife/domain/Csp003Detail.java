package com.twfhclife.eservice.api.elife.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Csp003Detail {
	@JsonProperty(value = "FS82-SCN-NAME")
	private String fs82ScnName   ;
	@JsonProperty(value = "FS82-FUNC-CODE")
	private String fs82FuncCode  ;
	@JsonProperty(value = "FS82-BRANCH-CODE")
	private String fs82BranchCode;
	@JsonProperty(value = "FS82-DP")
	private String fs82Dp        ;
	@JsonProperty(value = "FS82-INSU-NO")
	private String fs82InsuNo    ;
	@JsonProperty(value = "FS82-VIRTUAL-NO")
	private String fs82VirtualNo ;
	public String getFs82ScnName() {
		return fs82ScnName;
	}
	public void setFs82ScnName(String fs82ScnName) {
		this.fs82ScnName = fs82ScnName;
	}
	public String getFs82FuncCode() {
		return fs82FuncCode;
	}
	public void setFs82FuncCode(String fs82FuncCode) {
		this.fs82FuncCode = fs82FuncCode;
	}
	public String getFs82BranchCode() {
		return fs82BranchCode;
	}
	public void setFs82BranchCode(String fs82BranchCode) {
		this.fs82BranchCode = fs82BranchCode;
	}
	public String getFs82Dp() {
		return fs82Dp;
	}
	public void setFs82Dp(String fs82Dp) {
		this.fs82Dp = fs82Dp;
	}
	public String getFs82InsuNo() {
		return fs82InsuNo;
	}
	public void setFs82InsuNo(String fs82InsuNo) {
		this.fs82InsuNo = fs82InsuNo;
	}
	public String getFs82VirtualNo() {
		return fs82VirtualNo;
	}
	public void setFs82VirtualNo(String fs82VirtualNo) {
		this.fs82VirtualNo = fs82VirtualNo;
	}
	
	
}
