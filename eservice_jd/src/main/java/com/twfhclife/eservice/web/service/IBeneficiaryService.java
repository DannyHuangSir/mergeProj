package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.BeneficiaryVo;

import java.util.List;

public interface IBeneficiaryService {

	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(String policyNo);
	
}
