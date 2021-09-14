package com.twfhclife.eservice.api.elife.domain;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.policy.model.CoverageVo;

public class BenefitDetailResponse {

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