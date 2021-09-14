package com.twfhclife.eservice_batch.model;

import java.math.BigDecimal;

public class RoiNotificationVo {

	private Integer id;
	private String policyNo; // 保單號碼
	private String fundCode; // 標的代號
	private Integer percentageUp; // 停利點
	private Integer percentageDown; // 停損點
	private BigDecimal upValue; // 現行淨值上限
	private BigDecimal downValue; // 現行淨值上限
	private String enabled;
	private InvestmentVo investmentVo; // 標的資訊
	private MyPortfolioVo portfolioVo; //報酬率

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Integer getPercentageUp() {
		return percentageUp;
	}

	public void setPercentageUp(Integer percentageUp) {
		this.percentageUp = percentageUp;
	}

	public Integer getPercentageDown() {
		return percentageDown;
	}

	public void setPercentageDown(Integer percentageDown) {
		this.percentageDown = percentageDown;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public InvestmentVo getInvestmentVo() {
		return investmentVo;
	}

	public void setInvestmentVo(InvestmentVo investmentVo) {
		this.investmentVo = investmentVo;
	}

	public MyPortfolioVo getPortfolioVo() {
		return portfolioVo;
	}

	public void setPortfolioVo(MyPortfolioVo portfolioVo) {
		this.portfolioVo = portfolioVo;
	}

	public BigDecimal getUpValue() {
		return upValue;
	}

	public void setUpValue(BigDecimal upValue) {
		this.upValue = upValue;
	}

	public BigDecimal getDownValue() {
		return downValue;
	}

	public void setDownValue(BigDecimal downValue) {
		this.downValue = downValue;
	}

}
