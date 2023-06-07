package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.LicohilVo;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailVo;

public interface ITransCtcSelectUtilService {
	
	public List<TransCtcSelectDataVo> getTransCtcSelectDataByLipmId(String lipmId); 
	
	public List<TransCtcSelectDetailVo> getTransCtcSelectDetailByLipmInsuSeqNo(String lipmInsuSeqNo);
	
	public List<TransCtcSelectDataAddCodeVo> getTransCtcSelectDataAddCode(String lipmId);
	
	public List<PolicyDetailVo> getPolicyDataByRocId(PolicyDetailRequest request);
	
	public List<LicohilVo> getLicohiByPolicyNo(PolicyDetailRequest request);
	
	public List<LicohilVo> getLilipmByPolicyNo(PolicyDetailRequest request);
}
