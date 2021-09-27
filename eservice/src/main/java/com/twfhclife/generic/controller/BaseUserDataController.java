package com.twfhclife.generic.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.generic.api_client.OnlineChangePolicyListClient;
import com.twfhclife.generic.api_client.UserPolicyListClient;
import com.twfhclife.generic.api_model.OnlineChangePolicyListResponse;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.controller.BaseController;

public class BaseUserDataController extends BaseController {

	private static final Logger logger = LogManager.getLogger(BaseUserDataController.class);
	
	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private OnlineChangePolicyListClient onlineChangePolicyListClient;
	
	@Autowired
	private UserPolicyListClient userPolicyListClient;
	
	/**
	 * 取得用戶線上申請保單清單.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PolicyListVo> getUserOnlineChangePolicyList(String userId, String rocId) {
		// 先嘗試從session取得
		List<PolicyListVo> policyList = null;
		if (getSession(UserDataInfo.USER_ALL_POLICY_LIST) != null) {
			logger.info("Get user[{}] onlineChangePolicyList data from session", userId);
			policyList = (List<PolicyListVo>) getSession(UserDataInfo.USER_ALL_POLICY_LIST);
		}
		
		if (CollectionUtils.isEmpty(policyList) && !StringUtils.isEmpty(rocId)) {
			// Call api 取得資料
			OnlineChangePolicyListResponse policyListResponse = onlineChangePolicyListClient
					.getOnlineChangePolicyList(userId, rocId);
			// 若無資料，嘗試由內部服務取得資料
			if (policyListResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getOnlineChangePolicyList]", userId);
				policyList = policyListResponse.getUserPolicyList();
			} else {
				logger.info("Call internal service to get user[{}] getOnlineChangePolicyList data", userId);
				policyList = policyListService.getUserPolicyList(rocId);
			}
		}
		return policyList;
	}
	
	/**
	 * 取得用戶線上申請保單清單(保單理賠).
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PolicyListVo> getUserOnlineChangePolicyListByRocId(String userId, String rocId) {
		// 為保證保單理賠加取的被保人保單清單正確，不再嘗試從session取得
		List<PolicyListVo> policyList = null;
		// Call api 取得資料
		OnlineChangePolicyListResponse policyListResponse = onlineChangePolicyListClient
				.getUserOnlineChangePolicyListByRocId(userId, rocId);
		// 若無資料，嘗試由內部服務取得資料
		if (policyListResponse != null) {
			logger.info("Get user[{}] data from eservice_api[getOnlineChangePolicyList]", userId);
			policyList = policyListResponse.getUserPolicyList();
		} else {
			logger.info("Call internal service policyListService.getUserPolicyListByRocId(rocId{}) to get policyList.", rocId);
			policyList = policyListService.getUserPolicyListByRocId(rocId);
		}
		return policyList;
	}


	/**
	 * 取得用戶線上申請 醫療
	 *
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PolicyListVo> getUserOnlineChangePolicyMedicalListByRocId(String userId, String rocId) {
		// 為保證保單理賠加取的被保人保單清單正確，不再嘗試從session取得
		List<PolicyListVo> policyList = null;
		// Call api 取得資料
		OnlineChangePolicyListResponse policyListResponse = onlineChangePolicyListClient
				.getUserOnlineChangePolicyToMedicalListByRocId(userId, rocId);
		// 若無資料，嘗試由內部服務取得資料
		if (policyListResponse != null) {
			logger.info("Get user[{}] data from eservice_api[getOnlineChangePolicyList]", userId);
			policyList = policyListResponse.getUserPolicyList();
		} else {
			logger.info("Call internal service policyListService.getUserPolicyListByRocId(rocId{}) to get policyList.", rocId);
			policyList = policyListService.getUserPolicyListByRocId(rocId);
		}
		return policyList;
	}


	
	/**
	 * 取得用戶投資、保障型保單清單.
	 * 
	 * @param userId 使用者代號
	 * @param rocId 使用者證號
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PolicyListResponse getPolicyListByUser(String userId, String rocId) {
		// 從session取得資料
		List<PolicyListVo> invtPolicyList = null;
		if (getSession(UserDataInfo.USER_INVT_POLICY_LIST) != null) {
			logger.info("Find user[{}] invtPolicyList from session", userId);
			invtPolicyList = (List<PolicyListVo>) getSession(UserDataInfo.USER_INVT_POLICY_LIST);
		}
		
		List<PolicyListVo> benefitPolicyList = null;
		if (getSession(UserDataInfo.USER_BENEFIT_POLICY_LIST) != null) {
			logger.info("Find user[{}] benefitPolicyList from session", userId);
			benefitPolicyList = (List<PolicyListVo>) getSession(UserDataInfo.USER_BENEFIT_POLICY_LIST);
		}
		
		// Call api 取得資料
		if (CollectionUtils.isEmpty(invtPolicyList) ||  CollectionUtils.isEmpty(benefitPolicyList)) {
			PolicyListResponse policyListResponse = userPolicyListClient.getPolicyListByUser(userId, rocId);
			if (policyListResponse != null) {
				invtPolicyList = policyListResponse.getInvtPolicyList();
				benefitPolicyList = policyListResponse.getBenefitPolicyList();
			}
		}
		
		// 投資型保單(若無資料，嘗試由內部服務取得資料)
		if (CollectionUtils.isEmpty(invtPolicyList)) {
			logger.info("Call internal service to get user[{}] invtPolicyList", userId);
			if (!StringUtils.isEmpty(rocId)) {
				invtPolicyList = policyListService.getInvtPolicyList(rocId);
			}
		}
		
		// 保障型保單(若無資料，嘗試由內部服務取得資料)
		if (CollectionUtils.isEmpty(benefitPolicyList)) {
			logger.info("Call internal service to get user[{}] benefitPolicyList", userId);
			if (!StringUtils.isEmpty(rocId)) {
				benefitPolicyList = policyListService.getBenefitPolicyList(rocId);
			}
		}
		
		PolicyListResponse userPolicyListResponse = new PolicyListResponse();
		userPolicyListResponse.setInvtPolicyList(invtPolicyList);
		userPolicyListResponse.setBenefitPolicyList(benefitPolicyList);
		
		return userPolicyListResponse;
	}
}
