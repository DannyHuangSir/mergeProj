package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.policy.model.PortfolioVo;

public class PortfolioResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

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