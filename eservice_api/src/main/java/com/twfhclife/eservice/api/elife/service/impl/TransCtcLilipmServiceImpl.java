package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.TransCtcLilipmDao;
import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmVo;

import com.twfhclife.eservice.api.elife.service.ITransCtcLilipmService;

@Service
public class TransCtcLilipmServiceImpl implements ITransCtcLilipmService{
	
	@Autowired
	public TransCtcLilipmDao dao;

	@Override
	public List<TransCtcLilipmVo> getCtcLilipm(String lipmId) {
		return dao.getCtcLilipm(lipmId);
	}

	@Override
	public List<TransCtcLilipmVo> getCtcLilipmDetail(String policyNo) {
		return dao.getCtcLilipmDetail(policyNo);
	}

	@Override
	public List<TransCtcLilipmVo> getRevokeByLilipmForLipmInsuSeqNo(String lipmInsuSeqNo) {
		return dao.getRevokeByLilipmForLipmInsuSeqNo(lipmInsuSeqNo);
	}

	@Override
	public List<TransCtcLilipmVo> getRevokeByLilipmForSeqNoAndAginRecCode(String lipmInsuSeqNo) {
		return dao.getRevokeByLilipmForSeqNoAndAginRecCode(lipmInsuSeqNo);
	}

	@Override
	public List<TransCtcLilipmVo> getOnlineTrialDetail(String lipmInsuSeqNo) {
		return dao.getOnlineTrialDetail(lipmInsuSeqNo.substring(0,2), lipmInsuSeqNo);
	}
}
