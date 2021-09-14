package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.eservice.policy.model.FundSwitchVo;

public class TransFundSwitchVo extends AbstractOnlineChangeModelBean {

	private static final long serialVersionUID = 1L;

	private String id;

	private String transNum;

	private List<FundSwitchVo> fundSwitchOutList;

	private List<FundSwitchVo> fundSwitchInList;

	private String switchOutJsonData;

	private String switchInJsonData;

	private BigDecimal totalOutAmt;

	private String riskLevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public List<FundSwitchVo> getFundSwitchOutList() {
		return fundSwitchOutList;
	}

	public void setFundSwitchOutList(List<FundSwitchVo> fundSwitchOutList) {
		this.fundSwitchOutList = fundSwitchOutList;
	}

	public List<FundSwitchVo> getFundSwitchInList() {
		return fundSwitchInList;
	}

	public void setFundSwitchInList(List<FundSwitchVo> fundSwitchInList) {
		this.fundSwitchInList = fundSwitchInList;
	}

	public String getSwitchOutJsonData() {
		return switchOutJsonData;
	}

	public void setSwitchOutJsonData(String switchOutJsonData) {
		this.switchOutJsonData = switchOutJsonData;
	}

	public String getSwitchInJsonData() {
		return switchInJsonData;
	}

	public void setSwitchInJsonData(String switchInJsonData) {
		this.switchInJsonData = switchInJsonData;
	}

	public BigDecimal getTotalOutAmt() {
		return totalOutAmt;
	}

	public void setTotalOutAmt(BigDecimal totalOutAmt) {
		this.totalOutAmt = totalOutAmt;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}