package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.policy.model.CoverageVo;

public class BenefitDetailResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 保障型的被保人資料
	 */
	private Map<String, List<CoverageVo>> benefitInsuredData;

	public Map<String, List<CoverageVo>> getBenefitInsuredData() {
		return benefitInsuredData;
	}

	public void setBenefitInsuredData(Map<String, List<CoverageVo>> benefitInsuredData) {
		this.benefitInsuredData = benefitInsuredData;
	}
}