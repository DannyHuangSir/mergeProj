package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.StreetNameDataVo;
/**
 * 中華郵政道路名稱API
 * @author chiawei
 */
public interface IChunghwaApiService {

	public List<StreetNameDataVo> getStreetNameData(String city , String region);
	
}
