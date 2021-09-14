package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PolicyBonusVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String policyNo;
	/** 起始日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date bonusStartDate;
	/** 終止日 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date bonusEndDate;
	/** 當期主約紅利 */
	private BigDecimal bonus;
	/** 累積紅利 */
	private BigDecimal accuBonus;
	/** 領取方式代碼 */
	private String takeCode;
	/** 領取方式 */
	private String takeCodeDesc;
	/** 領取日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date takeDate;
	/** 紅利餘額 */
	private BigDecimal bonusBalance;
	/** 去年利息合計 */
	private BigDecimal bonusIni;
	/** 附約當期紅利加總 */
	private BigDecimal bonusRider;
	/** 購買保額之給付方式 */
	private String payCode;
	
	private PolicyVo policyVo;
	
	private IndividualVo individualVo;
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public Date getBonusStartDate() {
		return this.bonusStartDate;
	}
	
	public void setBonusStartDate(Date bonusStartDate) {
		this.bonusStartDate = bonusStartDate;
	}
	
	public Date getBonusEndDate() {
		return this.bonusEndDate;
	}
	
	public void setBonusEndDate(Date bonusEndDate) {
		this.bonusEndDate = bonusEndDate;
	}
	
	public BigDecimal getBonus() {
		return this.bonus;
	}
	
	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}
	
	public BigDecimal getAccuBonus() {
		return this.accuBonus;
	}
	
	public void setAccuBonus(BigDecimal accuBonus) {
		this.accuBonus = accuBonus;
	}
	
	public String getTakeCode() {
		return this.takeCode;
	}
	
	public void setTakeCode(String takeCode) {
		this.takeCode = takeCode;
	}
	
	public String getTakeCodeDesc() {
		return takeCodeDesc;
	}

	public void setTakeCodeDesc(String takeCodeDesc) {
		this.takeCodeDesc = takeCodeDesc;
	}

	public Date getTakeDate() {
		return this.takeDate;
	}
	
	public void setTakeDate(Date takeDate) {
		this.takeDate = takeDate;
	}
	
	public BigDecimal getBonusBalance() {
		return this.bonusBalance;
	}
	
	public void setBonusBalance(BigDecimal bonusBalance) {
		this.bonusBalance = bonusBalance;
	}
	
	public BigDecimal getBonusIni() {
		return this.bonusIni;
	}
	
	public void setBonusIni(BigDecimal bonusIni) {
		this.bonusIni = bonusIni;
	}
	
	public BigDecimal getBonusRider() {
		return this.bonusRider;
	}
	
	public void setBonusRider(BigDecimal bonusRider) {
		this.bonusRider = bonusRider;
	}
	
	public String getPayCode() {
		return this.payCode;
	}
	
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public PolicyVo getPolicyVo() {
		return policyVo;
	}

	public void setPolicyVo(PolicyVo policyVo) {
		this.policyVo = policyVo;
	}

	public IndividualVo getIndividualVo() {
		return individualVo;
	}

	public void setIndividualVo(IndividualVo individualVo) {
		this.individualVo = individualVo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}