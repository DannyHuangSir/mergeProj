package com.twfhclife.eservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PolicyExtraVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**  */
	private String policyNo;
	/**  */
	private BigDecimal loanAmount;
	/**  */
	private BigDecimal remainLoanValue;
	/**  */
	private BigDecimal roanRate;
	/**  */
	private BigDecimal aplAmount;
	/**  */
	private String autoRcpMk;
	/**  */
	private String methAnnuPay;
	
	private PolicyVo policyVo;
	
	private String inmsBankCode;
	
	private String inmsBankBranchCode;
	
	private String inmsAccountNo;
	
	private BigDecimal val1ReseAmt;
	
	private BigDecimal val1CancAmt;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	private Date modifyDatetime;
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public BigDecimal getLoanAmount() {
		return this.loanAmount;
	}
	
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	public BigDecimal getRemainLoanValue() {
		return this.remainLoanValue;
	}
	
	public void setRemainLoanValue(BigDecimal remainLoanValue) {
		this.remainLoanValue = remainLoanValue;
	}
	
	public BigDecimal getRoanRate() {
		return this.roanRate;
	}
	
	public void setRoanRate(BigDecimal roanRate) {
		this.roanRate = roanRate;
	}
	
	public BigDecimal getAplAmount() {
		return this.aplAmount;
	}
	
	public void setAplAmount(BigDecimal aplAmount) {
		this.aplAmount = aplAmount;
	}

	public PolicyVo getPolicyVo() {
		return policyVo;
	}

	public void setPolicyVo(PolicyVo policyVo) {
		this.policyVo = policyVo;
	}
	
	public String getAutoRcpMk() {
		return autoRcpMk;
	}

	public void setAutoRcpMk(String autoRcpMk) {
		this.autoRcpMk = autoRcpMk;
	}

	public String getMethAnnuPay() {
		return methAnnuPay;
	}

	public void setMethAnnuPay(String methAnnuPay) {
		this.methAnnuPay = methAnnuPay;
	}

	public String getInmsBankCode() {
		return inmsBankCode;
	}

	public void setInmsBankCode(String inmsBankCode) {
		this.inmsBankCode = inmsBankCode;
	}

	public String getInmsBankBranchCode() {
		return inmsBankBranchCode;
	}

	public void setInmsBankBranchCode(String inmsBankBranchCode) {
		this.inmsBankBranchCode = inmsBankBranchCode;
	}

	public String getInmsAccountNo() {
		return inmsAccountNo;
	}

	public void setInmsAccountNo(String inmsAccountNo) {
		this.inmsAccountNo = inmsAccountNo;
	}

	public BigDecimal getVal1ReseAmt() {
		return val1ReseAmt;
	}

	public void setVal1ReseAmt(BigDecimal val1ReseAmt) {
		this.val1ReseAmt = val1ReseAmt;
	}

	public BigDecimal getVal1CancAmt() {
		return val1CancAmt;
	}

	public void setVal1CancAmt(BigDecimal val1CancAmt) {
		this.val1CancAmt = val1CancAmt;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}