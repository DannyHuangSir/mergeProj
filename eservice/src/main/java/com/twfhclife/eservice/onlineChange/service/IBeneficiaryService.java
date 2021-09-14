package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.web.model.BeneficiaryVo;

public interface IBeneficiaryService {

	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(String policyNo);
	
}
