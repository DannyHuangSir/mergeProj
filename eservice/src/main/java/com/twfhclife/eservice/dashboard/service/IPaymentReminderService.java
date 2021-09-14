package com.twfhclife.eservice.dashboard.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.PolicyExtraVo;

/**
 * 繳費提醒服務.
 * 
 * @author alan
 */
public interface IPaymentReminderService {

	/**
	 * 取得繳費提醒.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳繳費提醒資料
	 */
	public List<PolicyExtraVo> getPaymentReminderList(String rocId);
}