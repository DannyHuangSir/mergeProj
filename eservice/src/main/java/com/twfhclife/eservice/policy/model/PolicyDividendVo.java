package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PolicyDividendVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String id;
	/** 保單號碼 */
	private String policyNo;
	/** 發放日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date clupTrDate;
	/** 帳戶單位 */
	private BigDecimal clupExpeUnits;
	/** 配息率 */
	private BigDecimal clupInprRate;
	/** 匯率 */
	private BigDecimal clupExchRate;
	/** 等值台/外幣 */
	private BigDecimal clupExpeNtd;
	/** 基金幣別 */
	private String invtCurr;
	/** 配息基準日(前一日): 資料庫是存放前一日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date inpdIntCompuDate;
	/** 配息基準日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date inpdIntDividendDate;
	/** 投資標的代碼 */
	private String invtNo;
	/** 投資標的名稱 */
	private String invtName;
	
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
	
	public Date getClupTrDate() {
		return this.clupTrDate;
	}
	
	public void setClupTrDate(Date clupTrDate) {
		this.clupTrDate = clupTrDate;
	}
	
	public BigDecimal getClupExpeUnits() {
		return this.clupExpeUnits;
	}
	
	public void setClupExpeUnits(BigDecimal clupExpeUnits) {
		this.clupExpeUnits = clupExpeUnits;
	}
	
	public BigDecimal getClupInprRate() {
		return this.clupInprRate;
	}
	
	public void setClupInprRate(BigDecimal clupInprRate) {
		this.clupInprRate = clupInprRate;
	}
	
	public BigDecimal getClupExchRate() {
		return this.clupExchRate;
	}
	
	public void setClupExchRate(BigDecimal clupExchRate) {
		this.clupExchRate = clupExchRate;
	}
	
	public BigDecimal getClupExpeNtd() {
		return this.clupExpeNtd;
	}
	
	public void setClupExpeNtd(BigDecimal clupExpeNtd) {
		this.clupExpeNtd = clupExpeNtd;
	}
	
	public String getInvtCurr() {
		return this.invtCurr;
	}
	
	public void setInvtCurr(String invtCurr) {
		this.invtCurr = invtCurr;
	}
	
	public Date getInpdIntCompuDate() {
		return this.inpdIntCompuDate;
	}
	
	public void setInpdIntCompuDate(Date inpdIntCompuDate) {
		this.inpdIntCompuDate = inpdIntCompuDate;
	}
	
	public Date getInpdIntDividendDate() {
		return this.inpdIntDividendDate;
	}
	
	public void setInpdIntDividendDate(Date inpdIntDividendDate) {
		this.inpdIntDividendDate = inpdIntDividendDate;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

