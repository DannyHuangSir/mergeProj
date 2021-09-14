package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.PolicyPaymentRecordDao;
import com.twfhclife.eservice.policy.model.PolicyPaymentRecordVo;
import com.twfhclife.eservice.policy.service.IPolicyPaymentRecordService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 繳費服務.
 * 
 * @author all
 */
@Service
public class PolicyPaymentRecordServiceImpl implements IPolicyPaymentRecordService {
	
	@Autowired
	private PolicyPaymentRecordDao policyPaymentRecordDao;
	
	/**
	 * 繳費-查詢.
	 * 
	 * @param policyPaymentRecordVo PolicyPaymentRecordVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyPaymentRecordVo> getPolicyPaymentRecord(PolicyPaymentRecordVo policyPaymentRecordVo) {
		return policyPaymentRecordDao.getPolicyPaymentRecord(policyPaymentRecordVo);
	}
	
	/**
	 * 繳費(最近一年)-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<PolicyPaymentRecordVo> getPolicyPaymentRecordLastYear(String policyNo) {
		return policyPaymentRecordDao.getPolicyPaymentRecordLastYear(policyNo);
	}
}