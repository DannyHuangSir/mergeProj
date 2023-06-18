package com.twfhclife.jd.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.jd.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PortfolioVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;

	/** 目前單位數 */
	private BigDecimal safpNetUnits;

	/** 目前金額 */
	private BigDecimal safpNetAmt;

	/** 平均交易幣別買價 */
	private BigDecimal safpAvgPval;

	/** 平均台幣買價 */
	private BigDecimal safpAvgPntdval;

	/** 投資標的代碼 */
	private String invtNo;

	/** 投資標的名稱 */
	private String invtName;

	/** 基金幣別 */
	private String invtExchCurr;
	
	/** 保單幣別 */
	private String insuCurr;

	/** 投資收益等級 */
	private String invtRiskBeneLevel;

	/** 契約狀況代碼 */
	private String invtSt;

	/** 單位淨值(賣出) */
	private BigDecimal netValueSell;

	/** 淨值交易日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date netValueDate;

	/** 買入收盤價 */
	private BigDecimal exchRateBuy;

	/** 匯兌日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date exchRateDate;

	/** 累計投資收益 */
	private BigDecimal clupExpeNtdSum;
	
	/** 幣別名稱 */
	private String currency;
	
	/** 參考報酬率(%) */
	private BigDecimal roiRate;
	
	/** 帳戶價值 */
	private BigDecimal acctValue;
	
	/** 平均成本 */
	private BigDecimal avgPval;
	
	/** 風險收益等級 */
	private String riskBeneLevel;

	private String ownPval;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public BigDecimal getSafpNetUnits() {
		return safpNetUnits;
	}

	public void setSafpNetUnits(BigDecimal safpNetUnits) {
		this.safpNetUnits = safpNetUnits;
	}

	public BigDecimal getSafpNetAmt() {
		return safpNetAmt;
	}

	public void setSafpNetAmt(BigDecimal safpNetAmt) {
		this.safpNetAmt = safpNetAmt;
	}

	public BigDecimal getSafpAvgPval() {
		return safpAvgPval;
	}

	public void setSafpAvgPval(BigDecimal safpAvgPval) {
		this.safpAvgPval = safpAvgPval;
	}

	public BigDecimal getSafpAvgPntdval() {
		return safpAvgPntdval;
	}

	public void setSafpAvgPntdval(BigDecimal safpAvgPntdval) {
		this.safpAvgPntdval = safpAvgPntdval;
	}

	public String getInvtNo() {
		return invtNo;
	}

	public void setInvtNo(String invtNo) {
		this.invtNo = invtNo;
	}

	public String getInvtName() {
		return invtName;
	}

	public void setInvtName(String invtName) {
		this.invtName = invtName;
	}

	public String getInvtExchCurr() {
		return invtExchCurr;
	}

	public void setInvtExchCurr(String invtExchCurr) {
		this.invtExchCurr = invtExchCurr;
	}

	public String getInvtRiskBeneLevel() {
		return invtRiskBeneLevel;
	}

	public void setInvtRiskBeneLevel(String invtRiskBeneLevel) {
		this.invtRiskBeneLevel = invtRiskBeneLevel;
	}

	public String getInvtSt() {
		return invtSt;
	}

	public void setInvtSt(String invtSt) {
		this.invtSt = invtSt;
	}

	public BigDecimal getNetValueSell() {
		return netValueSell;
	}

	public void setNetValueSell(BigDecimal netValueSell) {
		this.netValueSell = netValueSell;
	}

	public String getNetValueDate() {
		return DateUtil.getRocDate(netValueDate);
	}

	public void setNetValueDate(Date netValueDate) {
		this.netValueDate = netValueDate;
	}

	public BigDecimal getExchRateBuy() {
		return exchRateBuy;
	}

	public void setExchRateBuy(BigDecimal exchRateBuy) {
		this.exchRateBuy = exchRateBuy;
	}

	public Date getExchRateDate() {
			return exchRateDate;
	}

	public void setExchRateDate(Date exchRateDate) {
		this.exchRateDate = exchRateDate;
	}

	public BigDecimal getClupExpeNtdSum() {
		return clupExpeNtdSum;
	}

	public void setClupExpeNtdSum(BigDecimal clupExpeNtdSum) {
		this.clupExpeNtdSum = clupExpeNtdSum;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getRoiRate() {
		return roiRate;
	}

	public void setRoiRate(BigDecimal roiRate) {
		this.roiRate = roiRate;
	}

	public BigDecimal getAcctValue() {
		return acctValue;
	}

	public void setAcctValue(BigDecimal acctValue) {
		this.acctValue = acctValue;
	}

	public BigDecimal getAvgPval() {
		return avgPval;
	}

	public void setAvgPval(BigDecimal avgPval) {
		this.avgPval = avgPval;
	}

	public String getInsuCurr() {
		return insuCurr;
	}

	public void setInsuCurr(String insuCurr) {
		this.insuCurr = insuCurr;
	}

	public String getRiskBeneLevel() {
		return riskBeneLevel;
	}

	public void setRiskBeneLevel(String riskBeneLevel) {
		this.riskBeneLevel = riskBeneLevel;
	}

	public String getOwnPval() {
		return ownPval;
	}

	public void setOwnPval(String ownPval) {
		this.ownPval = ownPval;
	}
}
