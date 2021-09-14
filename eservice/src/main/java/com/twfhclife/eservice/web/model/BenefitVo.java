package com.twfhclife.eservice.web.model;

import java.io.Serializable;

public class BenefitVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String benefitName;
	
	private String benefitDetail;

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getBenefitDetail() {
		return benefitDetail;
	}

	public void setBenefitDetail(String benefitDetail) {
		this.benefitDetail = benefitDetail;
	}
}
