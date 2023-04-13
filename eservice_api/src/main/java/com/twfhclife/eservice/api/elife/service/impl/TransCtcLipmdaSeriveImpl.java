package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLipmdaDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLipmdaService;

@Service
public class TransCtcLipmdaSeriveImpl implements ITransCtcLipmdaService{

	@Autowired
	public TransCtcLipmdaDao transCtcLipmdaDao;
		
	@Override
	public List<TransCtcLipmdaVo> getCtcLipmda(String lipmId) {
		return transCtcLipmdaDao.getLipmda(lipmId);
	}

}
