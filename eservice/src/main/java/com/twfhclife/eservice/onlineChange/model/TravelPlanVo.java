package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 旅平險方案內容
 * 
 * @author Cathy
 *
 */
public class TravelPlanVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 方案類型  */
	private String planType;

	/** 方案類型Parameter */
	private String planTypeStr;

	/** 附加內容 */
	private String additional;

	/** 要保人年齡 */
	private Integer age;

	/** 保費-計算出 */
	private BigDecimal premium;

	/** 身故或殘廢 保險金額 */
	private BigDecimal addid;
	
	/** 保費 */
	private BigDecimal addidPremium;

	/** 意外傷害醫療給付保額 */
	private BigDecimal mrid;
	
	/** 意外傷害醫療給付保費 */
	private BigDecimal mridPremium;

	/** 海外突發醫療給付保額 */
	private BigDecimal orid;
	
	/** 意外傷害醫療給付保費 */
	private BigDecimal oridPremium;

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getPlanTypeStr() {
		return planTypeStr;
	}

	public void setPlanTypeStr(String planTypeStr) {
		this.planTypeStr = planTypeStr;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public BigDecimal getPremium() {
		return premium;
	}

	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	public BigDecimal getAddid() {
		return addid;
	}

	public void setAddid(BigDecimal addid) {
		this.addid = addid;
	}

	public BigDecimal getMrid() {
		return mrid;
	}

	public void setMrid(BigDecimal mrid) {
		this.mrid = mrid;
	}

	public BigDecimal getOrid() {
		return orid;
	}

	public void setOrid(BigDecimal orid) {
		this.orid = orid;
	}

	public BigDecimal getAddidPremium() {
		return addidPremium;
	}

	public void setAddidPremium(BigDecimal addidPremium) {
		this.addidPremium = addidPremium;
	}

	public BigDecimal getMridPremium() {
		return mridPremium;
	}

	public void setMridPremium(BigDecimal mridPremium) {
		this.mridPremium = mridPremium;
	}

	public BigDecimal getOridPremium() {
		return oridPremium;
	}

	public void setOridPremium(BigDecimal oridPremium) {
		this.oridPremium = oridPremium;
	}

}
