package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyBonusDao;
import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;

/**
 * 保單紅利服務.
 * 
 * @author all
 */
@Service
public class PolicyBonusServiceImpl implements IPolicyBonusService {

	@Autowired
	private PolicyBonusDao policyBonusDao;
	
	/**
	 * 保單紅利-查詢.
	 * 
	 * @param policyBonusVo PolicyBonusVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyBonusVo> getPolicyBonus(PolicyBonusVo policyBonusVo) {
		return policyBonusDao.getPolicyBonus(policyBonusVo);
	}
	
	/**
	 * 保單紅利-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyBonusVo> findByPolicyNo(String policyNo) {
		return policyBonusDao.findByPolicyNo(policyNo);
	}
	
	/**
	 * 查詢紅利選擇權.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳紅利選擇權
	 */
	@RequestLog
	@Override
	public String getBounsTake(String policyNo) {
		return policyBonusDao.getBounsTake(policyNo);
	}
}