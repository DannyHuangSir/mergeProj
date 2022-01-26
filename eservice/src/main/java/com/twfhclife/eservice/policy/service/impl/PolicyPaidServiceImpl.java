package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyPaidDao;
import com.twfhclife.eservice.policy.model.PolicyPaidVo;
import com.twfhclife.eservice.policy.service.IPolicyPaidService;

/**
 * 保單紅利服務.
 * 
 * @author all
 */
@Service
public class PolicyPaidServiceImpl implements IPolicyPaidService {

	@Autowired
	private PolicyPaidDao policyPaidDao;
	
	/**
	 * 保單給付-查詢.
	 * 
	 * @param policyPaidVo PolicyPaidVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyPaidVo> getPolicyPaid(PolicyPaidVo policyPaidVo) {
		return policyPaidDao.getPolicyPaid(policyPaidVo);
	}
	
	/**
	 * 保單給付-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyPaidVo> findByPolicyNo(String policyNo) {
		return policyPaidDao.findByPolicyNo(policyNo);
	}
	
}