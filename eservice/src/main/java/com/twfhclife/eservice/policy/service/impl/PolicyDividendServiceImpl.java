package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyDividendDao;
import com.twfhclife.eservice.policy.model.PolicyDividendVo;
import com.twfhclife.eservice.policy.service.IPolicyDividendService;

/**
 * 保單收益分配服務.
 * 
 * @author all
 */
@Service
public class PolicyDividendServiceImpl implements IPolicyDividendService {

	@Autowired
	private PolicyDividendDao policyDividendDao;
	
	/**
	 * 保單收益分配-查詢.
	 * 
	 * @param policyDividendVo PolicyDividendVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyDividendVo> getPolicyDividend(PolicyDividendVo policyDividendVo) {
		return policyDividendDao.getPolicyDividend(policyDividendVo);
	}
}