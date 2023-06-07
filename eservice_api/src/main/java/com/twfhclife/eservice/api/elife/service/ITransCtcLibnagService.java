package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLibnagVo;

public interface ITransCtcLibnagService {
	
	public List<TransCtcLibnagVo> getRevokeByLibnagForBnagInsuSeqNo(String bnagInsuSeqNo);

	public List<TransCtcLibnagVo> getBirthByPolicyNo(String policyNo);
}
