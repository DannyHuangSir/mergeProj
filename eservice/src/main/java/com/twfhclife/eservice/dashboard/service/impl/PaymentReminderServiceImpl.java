package com.twfhclife.eservice.dashboard.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.dashboard.service.IPaymentReminderService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.PolicyExtraDao;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;

/**
 * 繳費提醒服務.
 * 
 * @author alan
 */
@Service
public class PaymentReminderServiceImpl implements IPaymentReminderService {

	@Autowired
	private PolicyExtraDao policyExtraDao;

	/**
	 * 取得繳費提醒.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳繳費提醒資料
	 */
	@RequestLog
	@Override
	public List<PolicyExtraVo> getPaymentReminderList(String rocId) {
		return policyExtraDao.getPaymentReminderList(rocId);
	}
}