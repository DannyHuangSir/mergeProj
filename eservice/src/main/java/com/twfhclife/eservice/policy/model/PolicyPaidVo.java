package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PolicyPaidVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;
	/** 給付種類 */
	private String payReas;
	/** 實付金額 */
	private Integer payAmount;
	/** 受款人 */
	private String payName;
	/** 給付方式 */
	private String payMethod;
	/** 給付日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date payDate;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPayReas() {
		return payReas;
	}

	public void setPayReas(String payReas) {
		this.payReas = payReas;
	}

	public Integer getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Integer payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}