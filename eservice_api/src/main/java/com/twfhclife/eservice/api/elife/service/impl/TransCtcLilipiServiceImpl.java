package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLilipiDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLilipiVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLilipiService;

@Service
public class TransCtcLilipiServiceImpl implements ITransCtcLilipiService{

	@Autowired
	private TransCtcLilipiDao dao;
		
	@Override
	public List<TransCtcLilipiVo> getLipiDataByPolicyNo(String lipiInsuNo) {
		return dao.getLipiDataByPolicyNo(lipiInsuNo);
	}

}
