package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BenefitVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String benefitCode;
	/**  */
	private String productCode;
	/**  */
	private String benefitName;
	/**  */
	private String benefitDetail;
	
	private String desiVersion;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date desiSaleDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date desiChanVarDate;
	
	public String getBenefitCode() {
		return this.benefitCode;
	}
	
	public void setBenefitCode(String benefitCode) {
		this.benefitCode = benefitCode;
	}
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getBenefitName() {
		return this.benefitName;
	}
	
	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}
	
	public String getBenefitDetail() {
		return this.benefitDetail;
	}
	
	public void setBenefitDetail(String benefitDetail) {
		this.benefitDetail = benefitDetail;
	}

	public String getDesiVersion() {
		return desiVersion;
	}

	public void setDesiVersion(String desiVersion) {
		this.desiVersion = desiVersion;
	}

	public Date getDesiSaleDate() {
		return desiSaleDate;
	}

	public void setDesiSaleDate(Date desiSaleDate) {
		this.desiSaleDate = desiSaleDate;
	}

	public Date getDesiChanVarDate() {
		return desiChanVarDate;
	}

	public void setDesiChanVarDate(Date desiChanVarDate) {
		this.desiChanVarDate = desiChanVarDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

