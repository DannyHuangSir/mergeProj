package com.twfhclife.eservice.api_model;


import com.twfhclife.eservice.web.model.PortfolioVo;

import java.util.List;

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