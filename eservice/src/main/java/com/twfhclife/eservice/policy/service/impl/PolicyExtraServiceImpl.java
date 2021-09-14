package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.PolicyExtraDao;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 保單貸款服務.
 * 
 * @author all
 */
@Service
public class PolicyExtraServiceImpl implements IPolicyExtraService {

	@Autowired
	private PolicyExtraDao policyExtraDao;
	
	/**
	 * 保單貸款-查詢.
	 * 
	 * @param policyExtraVo PolicyExtraVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyExtraVo> getPolicyExtra(PolicyExtraVo policyExtraVo) {
		return policyExtraDao.getPolicyExtra(policyExtraVo);
	}
	
	/**
	 * 保單貸款-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public PolicyExtraVo findByPolicyNo(String policyNo) {
		return policyExtraDao.findByPolicyNo(policyNo);
	}

	/**
	 * 取得使用者的資產與負債.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public PolicyExtraVo getUserAllLoanData(String rocId) {
		return policyExtraDao.getUserAllLoanData(rocId);
	}
}