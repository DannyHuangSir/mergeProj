package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.dao.BeneficiaryDao;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.web.model.BeneficiaryVo;

@Service
public class BeneficiaryServiceImpl implements IBeneficiaryService {
	
	@Autowired
	private BeneficiaryDao beneficiaryDao;

	@Override
	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(String policyNo){
		return beneficiaryDao.getBeneficiaryByPolicyNo(policyNo);
	}
}