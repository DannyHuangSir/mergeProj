package com.twfhclife.jd.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

public class FundPrdtVo extends PageInfoVo {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/**  */
	private String prdtInsuNo;
	/**  */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date prdtBookDate;
	/**  */
	private BigDecimal prdtRcpAmt;
	/**  */
	private BigDecimal prdtInvestAmt;
	/**  */
	private BigDecimal prdtIncreLoading;
	/**  */
	private BigDecimal prdtTargetLoading;
	/**  */
	private BigDecimal cost;
	/**  */
	private BigDecimal maintenanceFee;
	/**  */
	private BigDecimal interest;
	
	private String startDate;
	
	private String endDate;
	
	private int rowNum;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPrdtInsuNo() {
		return this.prdtInsuNo;
	}
	
	public void setPrdtInsuNo(String prdtInsuNo) {
		this.prdtInsuNo = prdtInsuNo;
	}
	
	public Date getPrdtBookDate() {
		return this.prdtBookDate;
	}
	
	public void setPrdtBookDate(Date prdtBookDate) {
		this.prdtBookDate = prdtBookDate;
	}
	
	public BigDecimal getPrdtRcpAmt() {
		return this.prdtRcpAmt;
	}
	
	public void setPrdtRcpAmt(BigDecimal prdtRcpAmt) {
		this.prdtRcpAmt = prdtRcpAmt;
	}
	
	public BigDecimal getPrdtInvestAmt() {
		return this.prdtInvestAmt;
	}
	
	public void setPrdtInvestAmt(BigDecimal prdtInvestAmt) {
		this.prdtInvestAmt = prdtInvestAmt;
	}
	
	public BigDecimal getPrdtIncreLoading() {
		return this.prdtIncreLoading;
	}
	
	public void setPrdtIncreLoading(BigDecimal prdtIncreLoading) {
		this.prdtIncreLoading = prdtIncreLoading;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getMaintenanceFee() {
		return maintenanceFee;
	}

	public void setMaintenanceFee(BigDecimal maintenanceFee) {
		this.maintenanceFee = maintenanceFee;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	
	public BigDecimal getPrdtTargetLoading() {
		return prdtTargetLoading;
	}

	public void setPrdtTargetLoading(BigDecimal prdtTargetLoading) {
		this.prdtTargetLoading = prdtTargetLoading;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}