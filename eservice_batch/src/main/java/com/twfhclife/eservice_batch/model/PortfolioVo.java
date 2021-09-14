package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioVo extends InvestmentVo{
	private BigDecimal roiRate; // 投資報酬率
	private BigDecimal expeNtd; // 累積投資收益 (CLUP_EXPE_NTD)
	private BigDecimal acctValue; // 帳戶價值(原幣)

	// 頁面顯示
	private BigDecimal netValue; // 頁面顯示淨值
	private BigDecimal exchRate; // 頁面顯示匯率
	private LocalDate netValueDate; // 最新淨值日期
	private LocalDate exchRateDate; // 最新匯率日期
	
	public BigDecimal getRoiRate() {
		return roiRate;
	}
	public void setRoiRate(BigDecimal roiRate) {
		this.roiRate = roiRate;
	}
	public BigDecimal getExpeNtd() {
		return expeNtd;
	}
	public void setExpeNtd(BigDecimal expeNtd) {
		this.expeNtd = expeNtd;
	}
	public BigDecimal getAcctValue() {
		return acctValue;
	}
	public void setAcctValue(BigDecimal acctValue) {
		this.acctValue = acctValue;
	}
	public BigDecimal getNetValue() {
		return netValue;
	}
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}
	public BigDecimal getExchRate() {
		return exchRate;
	}
	public void setExchRate(BigDecimal exchRate) {
		this.exchRate = exchRate;
	}
	public LocalDate getNetValueDate() {
		return netValueDate;
	}
	public void setNetValueDate(LocalDate netValueDate) {
		this.netValueDate = netValueDate;
	}
	public LocalDate getExchRateDate() {
		return exchRateDate;
	}
	public void setExchRateDate(LocalDate exchRateDate) {
		this.exchRateDate = exchRateDate;
	}

}