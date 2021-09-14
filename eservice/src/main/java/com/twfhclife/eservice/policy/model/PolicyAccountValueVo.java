package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.eservice.web.model.PageInfoVo;

public class PolicyAccountValueVo extends PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	/** 保單號碼 */
	private String poevInsuNo;
	/** 投資標的代碼 */
	private String fundCode;
	/** 投資標的名稱 */
	private String fundName;
	/** 投資標的幣別 */
	private String currency;
	/** 計算日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date poevEvalDate;
	/** 平均成本(A) */
	private BigDecimal poevAvgPntdval;
	/** 單位數/金額(B) */
	private BigDecimal poevSafpUnits;
	/** 參考單位淨值(C) */
	private BigDecimal poevInprRate;
	/** 參考匯率(D) */
	private BigDecimal poevStateExchRate;
	/** 參考價戶價值(E=B*C*D) */
	private BigDecimal accountValue;
	/** 未實現損益(F=E-A) */
	private BigDecimal cost;
	/** 參考報酬率(G=F/A) */
	private BigDecimal roi;
	/** 平均成本總計sum(A) */
	private BigDecimal sumPoevAvgPntdval;
	/** 參考價戶價值總計sum(E) */
	private BigDecimal sumAccountValue;
	/** 未實現損益總計sum(F) */
	private BigDecimal sumCost;
	/** 參考報酬率總計sum(F)/sum(A) */
	private BigDecimal sumRoi;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPoevInsuNo() {
		return poevInsuNo;
	}
	public void setPoevInsuNo(String poevInsuNo) {
		this.poevInsuNo = poevInsuNo;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getPoevEvalDate() {
		return poevEvalDate;
	}
	public void setPoevEvalDate(Date poevEvalDate) {
		this.poevEvalDate = poevEvalDate;
	}
	public BigDecimal getPoevAvgPntdval() {
		return poevAvgPntdval;
	}
	public void setPoevAvgPntdval(BigDecimal poevAvgPntdval) {
		this.poevAvgPntdval = poevAvgPntdval;
	}
	public BigDecimal getPoevSafpUnits() {
		return poevSafpUnits;
	}
	public void setPoevSafpUnits(BigDecimal poevSafpUnits) {
		this.poevSafpUnits = poevSafpUnits;
	}
	public BigDecimal getPoevInprRate() {
		return poevInprRate;
	}
	public void setPoevInprRate(BigDecimal poevInprRate) {
		this.poevInprRate = poevInprRate;
	}
	public BigDecimal getPoevStateExchRate() {
		return poevStateExchRate;
	}
	public void setPoevStateExchRate(BigDecimal poevStateExchRate) {
		this.poevStateExchRate = poevStateExchRate;
	}
	public BigDecimal getAccountValue() {
		return accountValue;
	}
	public void setAccountValue(BigDecimal accountValue) {
		this.accountValue = accountValue;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public BigDecimal getRoi() {
		return roi;
	}
	public void setRoi(BigDecimal roi) {
		this.roi = roi;
	}
	public BigDecimal getSumPoevAvgPntdval() {
		return sumPoevAvgPntdval;
	}
	public void setSumPoevAvgPntdval(BigDecimal sumPoevAvgPntdval) {
		this.sumPoevAvgPntdval = sumPoevAvgPntdval;
	}
	public BigDecimal getSumAccountValue() {
		return sumAccountValue;
	}
	public void setSumAccountValue(BigDecimal sumAccountValue) {
		this.sumAccountValue = sumAccountValue;
	}
	public BigDecimal getSumCost() {
		return sumCost;
	}
	public void setSumCost(BigDecimal sumCost) {
		this.sumCost = sumCost;
	}
	public BigDecimal getSumRoi() {
		return sumRoi;
	}
	public void setSumRoi(BigDecimal sumRoi) {
		this.sumRoi = sumRoi;
	}
	
}
