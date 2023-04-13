package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLineacVo;

public interface ITransCtcLineacService {

	public List<TransCtcLineacVo> getRevokeByLineacForNeacInsuNo(String insuNo);
	
}
