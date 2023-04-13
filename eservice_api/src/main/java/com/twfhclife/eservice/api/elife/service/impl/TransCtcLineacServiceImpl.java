package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLineacDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLineacVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLineacService;


@Service
public class TransCtcLineacServiceImpl implements ITransCtcLineacService{
	
	@Autowired
	public TransCtcLineacDao transCtcLineacDao;

	@Override
	public List<TransCtcLineacVo> getRevokeByLineacForNeacInsuNo(String insuNo) {
		return transCtcLineacDao.getRevokeByLineacForNeacInsuNo(insuNo);
	}

}
