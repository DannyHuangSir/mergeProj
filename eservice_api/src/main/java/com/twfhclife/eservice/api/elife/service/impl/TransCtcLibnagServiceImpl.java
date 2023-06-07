package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLibnagDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLibnagVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLibnagService;

@Service
public class TransCtcLibnagServiceImpl implements ITransCtcLibnagService {

	@Autowired
	private TransCtcLibnagDao dao;
	
	@Override
	public List<TransCtcLibnagVo> getRevokeByLibnagForBnagInsuSeqNo(String bnagInsuSeqNo) {
		return dao.getRevokeByLibnagForBnagInsuSeqNo(bnagInsuSeqNo);
	}

	@Override
	public List<TransCtcLibnagVo> getBirthByPolicyNo(String policyNo) {
		return dao.getBirthByPolicyNo(policyNo);
	}

}
