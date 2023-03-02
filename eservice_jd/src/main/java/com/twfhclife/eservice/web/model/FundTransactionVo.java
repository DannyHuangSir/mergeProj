package com.twfhclife.eservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FundTransactionVo extends PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/** 保單號碼 */
	private String policyNo;
	/** 交易日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date sadpTrDate;
	/** 交易種類 */
	private String sadpTrCode;
	/** 交易原因 */
	private String sadpTrType;
	/** 單位數 */
	private BigDecimal sadpTrUnits;
	/** 贖回(匯兌後總金額) */
	private BigDecimal sadpAfexchAmt;
	/** 匯兌日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date sadpExchDate;
	/**  */
	private BigDecimal sadpApplyAmt;
	/**  */
	private Date sadpTripDate;
	/**  */
	private BigDecimal sadpSacntLefbf;
	/** 投資標的代碼 */
	private String invtNo;
	/** 投資標的名稱 */
	private String invtName;
	/** 基金幣別 */
	private String invtExchCurr;
	/** 淨值 */
	private BigDecimal netValue;
	/** 匯率 */
	private BigDecimal exchRate;
	
	private String sadpTrName;
	private BigDecimal exchangeRate;
	private BigDecimal fundValue;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public Date getSadpTrDate() {
		return this.sadpTrDate;
	}
	
	public void setSadpTrDate(Date sadpTrDate) {
		this.sadpTrDate = sadpTrDate;
	}
	
	public String getSadpTrCode() {
		return this.sadpTrCode;
	}
	
	public void setSadpTrCode(String sadpTrCode) {
		this.sadpTrCode = sadpTrCode;
	}
	
	public String getSadpTrType() {
		return this.sadpTrType;
	}
	
	public void setSadpTrType(String sadpTrType) {
		this.sadpTrType = sadpTrType;
	}
	
	public BigDecimal getSadpTrUnits() {
		return this.sadpTrUnits;
	}
	
	public void setSadpTrUnits(BigDecimal sadpTrUnits) {
		this.sadpTrUnits = sadpTrUnits;
	}
	
	public BigDecimal getSadpAfexchAmt() {
		return this.sadpAfexchAmt;
	}
	
	public void setSadpAfexchAmt(BigDecimal sadpAfexchAmt) {
		this.sadpAfexchAmt = sadpAfexchAmt;
	}
	
	public Date getSadpExchDate() {
		return this.sadpExchDate;
	}
	
	public void setSadpExchDate(Date sadpExchDate) {
		this.sadpExchDate = sadpExchDate;
	}
	
	public String getInvtNo() {
		return this.invtNo;
	}
	
	public void setInvtNo(String invtNo) {
		this.invtNo = invtNo;
	}
	
	public String getInvtName() {
		return this.invtName;
	}
	
	public void setInvtName(String invtName) {
		this.invtName = invtName;
	}
	
	public String getInvtExchCurr() {
		return this.invtExchCurr;
	}
	
	public void setInvtExchCurr(String invtExchCurr) {
		this.invtExchCurr = invtExchCurr;
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

	public BigDecimal getSadpApplyAmt() {
		return sadpApplyAmt;
	}

	public void setSadpApplyAmt(BigDecimal sadpApplyAmt) {
		this.sadpApplyAmt = sadpApplyAmt;
	}

	public Date getSadpTripDate() {
		return sadpTripDate;
	}

	public void setSadpTripDate(Date sadpTripDate) {
		this.sadpTripDate = sadpTripDate;
	}

	public BigDecimal getSadpSacntLefbf() {
		return sadpSacntLefbf;
	}

	public void setSadpSacntLefbf(BigDecimal sadpSacntLefbf) {
		this.sadpSacntLefbf = sadpSacntLefbf;
	}

	public String getSadpTrName() {
		return sadpTrName;
	}

	public void setSadpTrName(String sadpTrName) {
		this.sadpTrName = sadpTrName;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getFundValue() {
		return fundValue;
	}

	public void setFundValue(BigDecimal fundValue) {
		this.fundValue = fundValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}