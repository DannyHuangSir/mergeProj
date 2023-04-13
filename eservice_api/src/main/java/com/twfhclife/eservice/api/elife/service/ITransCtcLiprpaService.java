package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaVo;

public interface ITransCtcLiprpaService {

	public List<TransCtcLiprpaVo> getRevokeByLiprpaForInsuSeqNo(String prodNo , String prpaInsuSeqNo);
}
