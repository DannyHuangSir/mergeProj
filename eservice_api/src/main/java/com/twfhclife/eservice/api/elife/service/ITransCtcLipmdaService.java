package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaVo;

public interface ITransCtcLipmdaService {

	/**
	 * 取得CTC資料
	 * @param lipmId 身分證
	 * @return 
	 */
	public List<TransCtcLipmdaVo> getCtcLipmda(String lipmId);

}


