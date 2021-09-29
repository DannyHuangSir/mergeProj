package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvestmentVo {
	// LISAFP
	private String insuNo;// 保單號碼
	private String invtCurr; // 幣別
	private BigDecimal netUnits; // 目前單位數
	private BigDecimal netAmt; // 目前金額
	private BigDecimal avgPval; // 平均交易幣別買價
	private BigDecimal avgPNTDval; // 平均台幣買價
	// LIINVT
	private String invtNo; // 投資標的代碼
	private String riskBeneLevel; // 投資收益等級
	private String name; // 名稱
	private String stCode; // 契約狀況代碼
	private String investNo; // 基金編號
	// LIINWG
	private BigDecimal invtWeigh; // 投資權重(%)
	private String redEmpOrder; // 贖回順位
	private String inwgCode; // 投資種類
	// LIPRVT
	private String prodNo; // 險別(商品代碼)
	// 業務邏輯
	private String overRisk; // 保戶風險屬性是否超過風險等級(用於投資輔助,商品查淨值&匯率)
	private BigDecimal netValue; // 頁面顯示淨值
	private BigDecimal exchRate; // 頁面顯示匯率
	private Date inNetValueDate; // 凈值日
	public String getInsuNo() {
		return insuNo;
	}
	public void setInsuNo(String insuNo) {
		this.insuNo = insuNo;
	}
	public String getInvtCurr() {
		return invtCurr;
	}
	public void setInvtCurr(String invtCurr) {
		this.invtCurr = invtCurr;
	}
	public BigDecimal getNetUnits() {
		return netUnits;
	}
	public void setNetUnits(BigDecimal netUnits) {
		this.netUnits = netUnits;
	}
	public BigDecimal getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(BigDecimal netAmt) {
		this.netAmt = netAmt;
	}
	public BigDecimal getAvgPval() {
		return avgPval;
	}
	public void setAvgPval(BigDecimal avgPval) {
		this.avgPval = avgPval;
	}
	public BigDecimal getAvgPNTDval() {
		return avgPNTDval;
	}
	public void setAvgPNTDval(BigDecimal avgPNTDval) {
		this.avgPNTDval = avgPNTDval;
	}
	public String getInvtNo() {
		return invtNo;
	}
	public void setInvtNo(String invtNo) {
		this.invtNo = invtNo;
	}
	public String getRiskBeneLevel() {
		return riskBeneLevel;
	}
	public void setRiskBeneLevel(String riskBeneLevel) {
		this.riskBeneLevel = riskBeneLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStCode() {
		return stCode;
	}
	public void setStCode(String stCode) {
		this.stCode = stCode;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public BigDecimal getInvtWeigh() {
		return invtWeigh;
	}
	public void setInvtWeigh(BigDecimal invtWeigh) {
		this.invtWeigh = invtWeigh;
	}
	public String getRedEmpOrder() {
		return redEmpOrder;
	}
	public void setRedEmpOrder(String redEmpOrder) {
		this.redEmpOrder = redEmpOrder;
	}
	public String getInwgCode() {
		return inwgCode;
	}
	public void setInwgCode(String inwgCode) {
		this.inwgCode = inwgCode;
	}
	public String getProdNo() {
		return prodNo;
	}
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}
	public String getOverRisk() {
		return overRisk;
	}
	public void setOverRisk(String overRisk) {
		this.overRisk = overRisk;
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

	public Date getInNetValueDate() {
		return inNetValueDate;
	}

	public void setInNetValueDate(Date inNetValueDate) {
		this.inNetValueDate = inNetValueDate;
	}
}
