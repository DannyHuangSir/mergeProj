package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcSelectUtilDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcSelectUtilService;

@Service
public class TransCtcSelectUtilServiceImpl implements ITransCtcSelectUtilService{
	
	@Autowired
	private TransCtcSelectUtilDao dao ;

	@Override
	public List<TransCtcSelectDataVo> getTransCtcSelectDataByLipmId(String lipmId) {
		return dao.getTransCtcSelectDataByLipmId(lipmId);
	}

	@Override
	public List<TransCtcSelectDetailVo> getTransCtcSelectDetailByLipmInsuSeqNo(String lipmInsuSeqNo) {
		return dao.getTransCtcSelectDetailByLipmInsuSeqNo(lipmInsuSeqNo);
	}

	@Override
	public List<TransCtcSelectDataAddCodeVo> getTransCtcSelectDataAddCode(String lipmId) {
		return dao.getTransCtcSelectDataAddCode(lipmId);
	}

}
