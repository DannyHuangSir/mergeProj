package com.twfhclife.eservice.user.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LilipmVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String lipmInsuNo;
	/**  */
	private String lipmInsuType;
	/**  */
	private BigDecimal lipmInsuGrpNo;
	/**  */
	private String lipmInsuSeqNo;
	/**  */
	private String lipmAgenCode;
	/**  */
	private String lipmAgenNaCode;
	/**  */
	private String lipmAgenCodeBranch;
	/**  */
	private String lipmRcpCode;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipmTredPaabDate;
	/**  */
	private String lipmRcpMethCode;
	/**  */
	private String lipmSt;
	/**  */
	private String lipm5gp;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipmInsuBegDate;
	/**  */
	private String lipmId;
	
	@JsonIgnore
	private String noHiddenLipmId;
	
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipmBirth;
	/**  */
	private String lipmName1;
	/**  */
	private String lipmName2;
	/**  */
	private String lipmAddr;
	
	/**
	 * add 2021/10/27
	 * 不隱碼地址
	 */
	private String lipmAddrNoHidden;
	
	/**  */
	private String lipmZipCode;
	/**  */
	private String lipmTelH;
	
	@JsonIgnore
	private String noHiddenLipmTelH;
	
	/**  */
	private String lipmTelO;
	
	@JsonIgnore
	private String noHiddenLipmTelO;
	
	/**  */
	private String lipmCharId;
	/**  */
	private String lipmCharAddr;
	
	/**
	 * add 2021/10/27
	 * 不隱碼收費地址
	 */
	private String lipmCharAddrNoHidden;
	/**  */
	private String lipmCharZipCode;
	/**  */
	private String lipmAutoRcpMk;
	
	private String lipmNameBase64;
	
	private String lipmFlexRcpMk;
	
	public String getLipmInsuNo() {
		return this.lipmInsuNo;
	}
	
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}
	
	public String getLipmInsuType() {
		return this.lipmInsuType;
	}
	
	public void setLipmInsuType(String lipmInsuType) {
		this.lipmInsuType = lipmInsuType;
	}
	
	public BigDecimal getLipmInsuGrpNo() {
		return this.lipmInsuGrpNo;
	}
	
	public void setLipmInsuGrpNo(BigDecimal lipmInsuGrpNo) {
		this.lipmInsuGrpNo = lipmInsuGrpNo;
	}
	
	public String getLipmInsuSeqNo() {
		return this.lipmInsuSeqNo;
	}
	
	public void setLipmInsuSeqNo(String lipmInsuSeqNo) {
		this.lipmInsuSeqNo = lipmInsuSeqNo;
	}
	
	public String getLipmAgenCode() {
		return this.lipmAgenCode;
	}
	
	public void setLipmAgenCode(String lipmAgenCode) {
		this.lipmAgenCode = lipmAgenCode;
	}
	
	public String getLipmAgenNaCode() {
		return this.lipmAgenNaCode;
	}
	
	public void setLipmAgenNaCode(String lipmAgenNaCode) {
		this.lipmAgenNaCode = lipmAgenNaCode;
	}
	
	public String getLipmAgenCodeBranch() {
		return this.lipmAgenCodeBranch;
	}
	
	public void setLipmAgenCodeBranch(String lipmAgenCodeBranch) {
		this.lipmAgenCodeBranch = lipmAgenCodeBranch;
	}
	
	public String getLipmRcpCode() {
		return this.lipmRcpCode;
	}
	
	public void setLipmRcpCode(String lipmRcpCode) {
		this.lipmRcpCode = lipmRcpCode;
	}
	
	public Date getLipmTredPaabDate() {
		return this.lipmTredPaabDate;
	}
	
	public void setLipmTredPaabDate(Date lipmTredPaabDate) {
		this.lipmTredPaabDate = lipmTredPaabDate;
	}
	
	public String getLipmRcpMethCode() {
		return this.lipmRcpMethCode;
	}
	
	public void setLipmRcpMethCode(String lipmRcpMethCode) {
		this.lipmRcpMethCode = lipmRcpMethCode;
	}
	
	public String getLipmSt() {
		return this.lipmSt;
	}
	
	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}
	
	public String getLipm5gp() {
		return this.lipm5gp;
	}
	
	public void setLipm5gp(String lipm5gp) {
		this.lipm5gp = lipm5gp;
	}
	
	public Date getLipmInsuBegDate() {
		return this.lipmInsuBegDate;
	}
	
	public void setLipmInsuBegDate(Date lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}
	
	public String getLipmId() {
		return this.lipmId;
	}
	
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}
	
	public Date getLipmBirth() {
		return this.lipmBirth;
	}
	
	public void setLipmBirth(Date lipmBirth) {
		this.lipmBirth = lipmBirth;
	}
	
	public String getLipmName1() {
		return this.lipmName1;
	}
	
	public void setLipmName1(String lipmName1) {
		this.lipmName1 = lipmName1;
	}
	
	public String getLipmName2() {
		return this.lipmName2;
	}
	
	public void setLipmName2(String lipmName2) {
		this.lipmName2 = lipmName2;
	}
	
	public String getLipmAddr() {
		return this.lipmAddr;
	}
	
	public void setLipmAddr(String lipmAddr) {
		this.lipmAddr = lipmAddr;
	}
	
	public String getLipmZipCode() {
		return this.lipmZipCode;
	}
	
	public void setLipmZipCode(String lipmZipCode) {
		this.lipmZipCode = lipmZipCode;
	}
	
	public String getLipmTelH() {
		return this.lipmTelH;
	}
	
	public void setLipmTelH(String lipmTelH) {
		this.lipmTelH = lipmTelH;
	}
	
	public String getLipmTelO() {
		return this.lipmTelO;
	}
	
	public void setLipmTelO(String lipmTelO) {
		this.lipmTelO = lipmTelO;
	}
	
	public String getLipmCharId() {
		return this.lipmCharId;
	}
	
	public void setLipmCharId(String lipmCharId) {
		this.lipmCharId = lipmCharId;
	}
	
	public String getLipmCharAddr() {
		return this.lipmCharAddr;
	}
	
	public void setLipmCharAddr(String lipmCharAddr) {
		this.lipmCharAddr = lipmCharAddr;
	}
	
	public String getLipmCharZipCode() {
		return this.lipmCharZipCode;
	}
	
	public void setLipmCharZipCode(String lipmCharZipCode) {
		this.lipmCharZipCode = lipmCharZipCode;
	}
	
	public String getLipmAutoRcpMk() {
		return this.lipmAutoRcpMk;
	}
	
	public void setLipmAutoRcpMk(String lipmAutoRcpMk) {
		this.lipmAutoRcpMk = lipmAutoRcpMk;
	}

	public String getNoHiddenLipmId() {
		return noHiddenLipmId;
	}

	public void setNoHiddenLipmId(String noHiddenLipmId) {
		this.noHiddenLipmId = noHiddenLipmId;
	}

	public String getNoHiddenLipmTelH() {
		return noHiddenLipmTelH;
	}

	public void setNoHiddenLipmTelH(String noHiddenLipmTelH) {
		this.noHiddenLipmTelH = noHiddenLipmTelH;
	}

	public String getNoHiddenLipmTelO() {
		return noHiddenLipmTelO;
	}

	public void setNoHiddenLipmTelO(String noHiddenLipmTelO) {
		this.noHiddenLipmTelO = noHiddenLipmTelO;
	}

	public String getLipmNameBase64() {
		return lipmNameBase64;
	}

	public void setLipmNameBase64(String lipmNameBase64) {
		this.lipmNameBase64 = lipmNameBase64;
	}

	public String getLipmFlexRcpMk() {
		return lipmFlexRcpMk;
	}

	public void setLipmFlexRcpMk(String lipmFlexRcpMk) {
		this.lipmFlexRcpMk = lipmFlexRcpMk;
	}

	public String getLipmAddrNoHidden() {
		return lipmAddrNoHidden;
	}

	public void setLipmAddrNoHidden(String lipmAddrNoHidden) {
		this.lipmAddrNoHidden = lipmAddrNoHidden;
	}

	public String getLipmCharAddrNoHidden() {
		return lipmCharAddrNoHidden;
	}

	public void setLipmCharAddrNoHidden(String lipmCharAddrNoHidden) {
		this.lipmCharAddrNoHidden = lipmCharAddrNoHidden;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}