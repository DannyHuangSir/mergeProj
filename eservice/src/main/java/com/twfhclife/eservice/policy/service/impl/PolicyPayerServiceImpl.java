package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyPayerDao;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.policy.service.IPolicyPayerService;
import com.twfhclife.generic.service.impl.BaseServiceImpl;

/**
 * 付款人服務.
 * 
 * @author all
 */
@Service
public class PolicyPayerServiceImpl extends BaseServiceImpl implements IPolicyPayerService {

	@Autowired
	private PolicyPayerDao policyPayerDao;
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyPayerVo PolicyPayerVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyPayerVo> getPolicyPayerList(PolicyPayerVo policyPayerVo) {
		return policyPayerDao.getPolicyPayerList(policyPayerVo);
	}
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public PolicyPayerVo getPolicyPayer(String policyNo) {
		PolicyPayerVo payer = policyPayerDao.getPolicyPayer(policyNo);
		if (payer != null) {
			payer.setPayerNameBase64(unicodeService.convertString2Unicode(payer.getPayerName()));
		}
		return payer;
	}
}