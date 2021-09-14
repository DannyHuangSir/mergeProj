package com.twfhclife.eservice.user.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LilipiVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String lipiInsuNo;
	/**  */
	private String lipiInsuType;
	/**  */
	private BigDecimal lipiInsuGrpNo;
	/**  */
	private String lipiInsuSeqNo;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipiInsuBegDate;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipiInsuEndDate;
	/**  */
	private String lipiGpNo;
	/**  */
	private String lipiId;
	
	@JsonIgnore
	private String noHiddenLipiId;
	/**  */
	private String lipiName;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipiBirth;
	/**  */
	private String lipiSex;
	/**  */
	private String lipiAge;
	/**  */
	private BigDecimal lipiPremYear;
	/**  */
	private String lipiMethAnnuPay;
	/**  */
	private BigDecimal lipiLiRate;
	/**  */
	private BigDecimal lipiMainAmt;
	/**  */
	private BigDecimal lipiTablPrem;
	/**  */
	private BigDecimal lipiUnHealPrem;
	/**  */
	private BigDecimal lipiDangPrem;
	/**  */
	private String lipiAddMk;
	/**  */
	private String lipiLoMk;
	/**  */
	private String lipiSt;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date lipiStDate;
	/**  */
	private BigDecimal lipiCancerQuan;
	/**  */
	private String lipiHealCode;
	/**  */
	private String lipiAddr;
	
	private String lipiNameBase64;
	
	public String getLipiInsuNo() {
		return this.lipiInsuNo;
	}
	
	public void setLipiInsuNo(String lipiInsuNo) {
		this.lipiInsuNo = lipiInsuNo;
	}
	
	public String getLipiInsuType() {
		return this.lipiInsuType;
	}
	
	public void setLipiInsuType(String lipiInsuType) {
		this.lipiInsuType = lipiInsuType;
	}
	
	public BigDecimal getLipiInsuGrpNo() {
		return this.lipiInsuGrpNo;
	}
	
	public void setLipiInsuGrpNo(BigDecimal lipiInsuGrpNo) {
		this.lipiInsuGrpNo = lipiInsuGrpNo;
	}
	
	public String getLipiInsuSeqNo() {
		return this.lipiInsuSeqNo;
	}
	
	public void setLipiInsuSeqNo(String lipiInsuSeqNo) {
		this.lipiInsuSeqNo = lipiInsuSeqNo;
	}
	
	public Date getLipiInsuBegDate() {
		return this.lipiInsuBegDate;
	}
	
	public void setLipiInsuBegDate(Date lipiInsuBegDate) {
		this.lipiInsuBegDate = lipiInsuBegDate;
	}
	
	public Date getLipiInsuEndDate() {
		return this.lipiInsuEndDate;
	}
	
	public void setLipiInsuEndDate(Date lipiInsuEndDate) {
		this.lipiInsuEndDate = lipiInsuEndDate;
	}
	
	public String getLipiGpNo() {
		return this.lipiGpNo;
	}
	
	public void setLipiGpNo(String lipiGpNo) {
		this.lipiGpNo = lipiGpNo;
	}
	
	public String getLipiId() {
		return this.lipiId;
	}
	
	public void setLipiId(String lipiId) {
		this.lipiId = lipiId;
	}
	
	public String getLipiName() {
		return this.lipiName;
	}
	
	public void setLipiName(String lipiName) {
		this.lipiName = lipiName;
	}
	
	public Date getLipiBirth() {
		return this.lipiBirth;
	}
	
	public void setLipiBirth(Date lipiBirth) {
		this.lipiBirth = lipiBirth;
	}
	
	public String getLipiSex() {
		return this.lipiSex;
	}
	
	public void setLipiSex(String lipiSex) {
		this.lipiSex = lipiSex;
	}
	
	public String getLipiAge() {
		return this.lipiAge;
	}
	
	public void setLipiAge(String lipiAge) {
		this.lipiAge = lipiAge;
	}
	
	public BigDecimal getLipiPremYear() {
		return this.lipiPremYear;
	}
	
	public void setLipiPremYear(BigDecimal lipiPremYear) {
		this.lipiPremYear = lipiPremYear;
	}

	public String getLipiMethAnnuPay() {
		return lipiMethAnnuPay;
	}

	public void setLipiMethAnnuPay(String lipiMethAnnuPay) {
		this.lipiMethAnnuPay = lipiMethAnnuPay;
	}

	public BigDecimal getLipiLiRate() {
		return this.lipiLiRate;
	}
	
	public void setLipiLiRate(BigDecimal lipiLiRate) {
		this.lipiLiRate = lipiLiRate;
	}
	
	public BigDecimal getLipiMainAmt() {
		return this.lipiMainAmt;
	}
	
	public void setLipiMainAmt(BigDecimal lipiMainAmt) {
		this.lipiMainAmt = lipiMainAmt;
	}
	
	public BigDecimal getLipiTablPrem() {
		return this.lipiTablPrem;
	}
	
	public void setLipiTablPrem(BigDecimal lipiTablPrem) {
		this.lipiTablPrem = lipiTablPrem;
	}
	
	public BigDecimal getLipiUnHealPrem() {
		return this.lipiUnHealPrem;
	}
	
	public void setLipiUnHealPrem(BigDecimal lipiUnHealPrem) {
		this.lipiUnHealPrem = lipiUnHealPrem;
	}
	
	public BigDecimal getLipiDangPrem() {
		return this.lipiDangPrem;
	}
	
	public void setLipiDangPrem(BigDecimal lipiDangPrem) {
		this.lipiDangPrem = lipiDangPrem;
	}
	
	public String getLipiAddMk() {
		return this.lipiAddMk;
	}
	
	public void setLipiAddMk(String lipiAddMk) {
		this.lipiAddMk = lipiAddMk;
	}
	
	public String getLipiLoMk() {
		return this.lipiLoMk;
	}
	
	public void setLipiLoMk(String lipiLoMk) {
		this.lipiLoMk = lipiLoMk;
	}
	
	public String getLipiSt() {
		return this.lipiSt;
	}
	
	public void setLipiSt(String lipiSt) {
		this.lipiSt = lipiSt;
	}
	
	public Date getLipiStDate() {
		return this.lipiStDate;
	}
	
	public void setLipiStDate(Date lipiStDate) {
		this.lipiStDate = lipiStDate;
	}
	
	public BigDecimal getLipiCancerQuan() {
		return this.lipiCancerQuan;
	}
	
	public void setLipiCancerQuan(BigDecimal lipiCancerQuan) {
		this.lipiCancerQuan = lipiCancerQuan;
	}
	
	public String getLipiHealCode() {
		return this.lipiHealCode;
	}
	
	public void setLipiHealCode(String lipiHealCode) {
		this.lipiHealCode = lipiHealCode;
	}
	
	public String getLipiAddr() {
		return this.lipiAddr;
	}
	
	public void setLipiAddr(String lipiAddr) {
		this.lipiAddr = lipiAddr;
	}

	public String getNoHiddenLipiId() {
		return noHiddenLipiId;
	}

	public void setNoHiddenLipiId(String noHiddenLipiId) {
		this.noHiddenLipiId = noHiddenLipiId;
	}

	public String getLipiNameBase64() {
		return lipiNameBase64;
	}

	public void setLipiNameBase64(String lipiNameBase64) {
		this.lipiNameBase64 = lipiNameBase64;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}