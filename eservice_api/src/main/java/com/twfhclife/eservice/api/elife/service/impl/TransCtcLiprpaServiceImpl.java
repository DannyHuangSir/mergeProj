package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLiprpaDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLiprpaService;

@Service
public class TransCtcLiprpaServiceImpl implements ITransCtcLiprpaService{

	@Autowired
	private TransCtcLiprpaDao transCtcLiprpaDao;
	
	@Override
	public List<TransCtcLiprpaVo> getRevokeByLiprpaForInsuSeqNo(String prodNo, String prpaInsuSeqNo) {
		return transCtcLiprpaDao.getRevokeByLiprpaForInsuSeqNo(prodNo, prpaInsuSeqNo);
	}

}
