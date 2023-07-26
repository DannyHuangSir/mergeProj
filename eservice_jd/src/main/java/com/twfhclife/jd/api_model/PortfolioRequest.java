package com.twfhclife.jd.api_model;

public class PortfolioRequest extends AbstractBasePolicyNoDomain {

	private static final long serialVersionUID = 1L;

	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}