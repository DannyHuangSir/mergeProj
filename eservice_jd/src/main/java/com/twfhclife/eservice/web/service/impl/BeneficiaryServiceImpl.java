package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.shouxian.dao.BeneficiaryDao;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.service.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryServiceImpl implements IBeneficiaryService {
	
	@Autowired
	private BeneficiaryDao beneficiaryDao;

	@Override
	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(String policyNo){
		return beneficiaryDao.getBeneficiaryByPolicyNo(policyNo);
	}
}