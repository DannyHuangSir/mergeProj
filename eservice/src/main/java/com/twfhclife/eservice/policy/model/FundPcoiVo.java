package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundPcoiVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/**  */
	private Date pcoiBookDate;
	/**  */
	private String pcoiInsuNo;
	/**  */
	private BigDecimal pcoiAmount;
	/**  */
	private BigDecimal pcoiInsuCost;
	/**  */
	private BigDecimal pcoiMonthCharge;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getPcoiBookDate() {
		return this.pcoiBookDate;
	}
	
	public void setPcoiBookDate(Date pcoiBookDate) {
		this.pcoiBookDate = pcoiBookDate;
	}
	
	public String getPcoiInsuNo() {
		return this.pcoiInsuNo;
	}
	
	public void setPcoiInsuNo(String pcoiInsuNo) {
		this.pcoiInsuNo = pcoiInsuNo;
	}
	
	public BigDecimal getPcoiAmount() {
		return this.pcoiAmount;
	}
	
	public void setPcoiAmount(BigDecimal pcoiAmount) {
		this.pcoiAmount = pcoiAmount;
	}
	
	public BigDecimal getPcoiInsuCost() {
		return this.pcoiInsuCost;
	}
	
	public void setPcoiInsuCost(BigDecimal pcoiInsuCost) {
		this.pcoiInsuCost = pcoiInsuCost;
	}
	
	public BigDecimal getPcoiMonthCharge() {
		return this.pcoiMonthCharge;
	}
	
	public void setPcoiMonthCharge(BigDecimal pcoiMonthCharge) {
		this.pcoiMonthCharge = pcoiMonthCharge;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}