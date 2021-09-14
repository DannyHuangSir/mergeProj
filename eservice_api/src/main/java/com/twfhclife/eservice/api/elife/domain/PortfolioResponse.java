package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.PortfolioVo;

public class PortfolioResponse {

	/**
	 * 投資損益及投報率清單資料
	 */
	private List<PortfolioVo> portfolioList;
	
	/**
	 * 投資風險屬性
	 */
	private String riskLevelName;

	public List<PortfolioVo> getPortfolioList() {
		return portfolioList;
	}

	public void setPortfolioList(List<PortfolioVo> portfolioList) {
		this.portfolioList = portfolioList;
	}

	public String getRiskLevelName() {
		return riskLevelName;
	}

	public void setRiskLevelName(String riskLevelName) {
		this.riskLevelName = riskLevelName;
	}
}