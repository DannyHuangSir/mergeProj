package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.eservice.web.model.PageInfoVo;

public class FundSwitchVo extends PageInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private String transNum;
	/** 投資標的代碼 */
	private String fundCode;
	/** 投資標的名稱 */
	private String fundName;
	/** 淨值 */
	private FundValueVo fundValueVo;
	/** 轉出/入百分比 */
	private BigDecimal switchPercentage;
	/** 轉出/入金額 */
	private BigDecimal switchAmount;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
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

	public FundValueVo getFundValueVo() {
		return fundValueVo;
	}

	public void setFundValueVo(FundValueVo fundValueVo) {
		this.fundValueVo = fundValueVo;
	}

	public BigDecimal getSwitchPercentage() {
		return switchPercentage;
	}

	public void setSwitchPercentage(BigDecimal switchPercentage) {
		this.switchPercentage = switchPercentage;
	}

	public BigDecimal getSwitchAmount() {
		return switchAmount;
	}

	public void setSwitchAmount(BigDecimal switchAmount) {
		this.switchAmount = switchAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}