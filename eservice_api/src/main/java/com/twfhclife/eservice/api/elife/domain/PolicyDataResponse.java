package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.policy.model.AgentVo;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.web.model.BeneficiaryVo;

public class PolicyDataResponse {

	/**
	 * 保項清單
	 */
	private List<CoverageVo> coverageList;
	
	/**
	 * 要保人
	 */
	private LilipmVo lilipmVo;
	
	/**
	 * 主被險人
	 */
	private LilipiVo lilipiVo;
	
	/**
	 * 付款人
	 */
	private PolicyPayerVo policyPayerVo;
	
	/**
	 * 招攬人
	 */
	private AgentVo agentVo;
	
	/**
	 * 受益人
	 */
	private List<BeneficiaryVo> beneficiaryList;

	public List<CoverageVo> getCoverageList() {
		return coverageList;
	}

	public void setCoverageList(List<CoverageVo> coverageList) {
		this.coverageList = coverageList;
	}

	public LilipmVo getLilipmVo() {
		return lilipmVo;
	}

	public void setLilipmVo(LilipmVo lilipmVo) {
		this.lilipmVo = lilipmVo;
	}

	public LilipiVo getLilipiVo() {
		return lilipiVo;
	}

	public void setLilipiVo(LilipiVo lilipiVo) {
		this.lilipiVo = lilipiVo;
	}

	public PolicyPayerVo getPolicyPayerVo() {
		return policyPayerVo;
	}

	public void setPolicyPayerVo(PolicyPayerVo policyPayerVo) {
		this.policyPayerVo = policyPayerVo;
	}

	public AgentVo getAgentVo() {
		return agentVo;
	}

	public void setAgentVo(AgentVo agentVo) {
		this.agentVo = agentVo;
	}

	public List<BeneficiaryVo> getBeneficiaryList() {
		return beneficiaryList;
	}

	public void setBeneficiaryList(List<BeneficiaryVo> beneficiaryList) {
		this.beneficiaryList = beneficiaryList;
	}
}