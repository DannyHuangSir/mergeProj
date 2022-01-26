package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyAccountValueDao;
import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;
import com.twfhclife.eservice.policy.service.IPolicyAccountValueService;

/**
 * 保單帳戶價值服務.
 * 
 * @author all
 */
@Service
public class PolicyAccountServiceImpl implements IPolicyAccountValueService {

	@Autowired
	private PolicyAccountValueDao policyAccountValueDao;
	
	@RequestLog
	@Override
	public List<PolicyAccountValueVo> getPolicyAccountValueList(String policyNo) {
		return policyAccountValueDao.getPolicyAccountValueList(policyNo);
	}

}
