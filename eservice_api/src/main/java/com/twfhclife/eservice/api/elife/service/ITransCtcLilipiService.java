package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLilipiVo;

public interface ITransCtcLilipiService {

	public List<TransCtcLilipiVo> getLipiDataByPolicyNo(String lipiInsuNo);
	
}
