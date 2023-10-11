package com.twfhclife.eservice.dashboard.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.twfhclife.eservice.dashboard.service.IDashboardService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.AuditLogVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.generic.api_client.DashboardClient;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_model.DashboardResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 保障總覽相關資料控制器.
 * 
 * @author alan
 */
@Controller
public class DashboardController extends BaseController {

	private static final Logger logger = LogManager.getLogger(DashboardController.class);

	@Autowired
	private IDashboardService dashboardService;

	@Autowired
	private IPolicyListService policyListService;

	@Autowired
	private IPortfolioService portfolioService;

	@Autowired
	private IPolicyExtraService policyExtraService;

	@Autowired
	private DashboardClient dashboardClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@Autowired
	private ILoginService loginService;

	/**
	 * 保障總覽頁面.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestLog
	@GetMapping(value = { "/", "index", "/dashboard" })
	public String dashboard() {
		try {
			if (!loginCheck()) {
				return "redirect:/login-timeout";
			}
		} catch (Exception e) {
			logger.error("Unable to loginCheck: {}", ExceptionUtils.getStackTrace(e));
		}
			
		try {
			String userRocId = getUserRocId();
			String userId = getUserId();
			
			if (!StringUtils.isEmpty(userRocId)) {
				// 保障型的被保人資料
				Map<String, List<String>> insuredProductData = null;
				// 保障型商品數量
				int benefitPolicyListSize = 0;
				// 投資型保單
				List<PolicyListVo> invtPolicyList = null;
				// 參考合計帳戶價值合計
				BigDecimal policyAcctValueTotal = BigDecimal.ZERO;
				// 我的資產＆負債
				PolicyExtraVo policyExtraVo = null;
				
				// 從session取得資料
				if (getSession(UserDataInfo.USER_DASHBOARD_DATA) != null) {
					logger.info("Find user[{}] dashboard data from session", userId);
					DashboardResponse dashboardResponse = (DashboardResponse) getSession(UserDataInfo.USER_DASHBOARD_DATA);
					if (dashboardResponse != null) {
						insuredProductData = dashboardResponse.getInsuredProductData();
						benefitPolicyListSize = dashboardResponse.getBenefitPolicyListSize();
						invtPolicyList = dashboardResponse.getInvtPolicyList();
						policyAcctValueTotal = dashboardResponse.getPolicyAcctValueTotal();
						policyExtraVo = dashboardResponse.getPolicyExtraVo();
						addSession(UserDataInfo.USER_DASHBOARD_DATA, dashboardResponse);
					}
				} else {
					/* 20230712 by 2033990 從api取得mark掉
					// Call api 取得資料
					DashboardResponse dashboardResponse = dashboardClient.getPolicyDashBoard(userId, userRocId);
					// 若無資料，嘗試由內部服務取得資料
					if (dashboardResponse != null) {
						insuredProductData = dashboardResponse.getInsuredProductData();
						benefitPolicyListSize = dashboardResponse.getBenefitPolicyListSize();
						invtPolicyList = dashboardResponse.getInvtPolicyList();
						policyAcctValueTotal = dashboardResponse.getPolicyAcctValueTotal();
						policyExtraVo = dashboardResponse.getPolicyExtraVo();
						addSession(UserDataInfo.USER_DASHBOARD_DATA, dashboardResponse);
					} else {
					*/
						logger.info("Call internal service to get user[{}] dashboard data", userId);
						
						// 保障型保單
						List<PolicyListVo> benefitPolicyList = null;
						if (getSession(UserDataInfo.USER_BENEFIT_POLICY_LIST) != null) {
							benefitPolicyList = (List<PolicyListVo>) getSession(UserDataInfo.USER_BENEFIT_POLICY_LIST);
						} else {
							benefitPolicyList = policyListService.getBenefitPolicyList(userRocId);
							addSession(UserDataInfo.USER_BENEFIT_POLICY_LIST, (Serializable) benefitPolicyList);
						}
						
						if (benefitPolicyList != null) {
							if (getSession(UserDataInfo.USER_INSURED_PRODUCT_DATA) != null) {
								insuredProductData = (Map<String, List<String>>) getSession(UserDataInfo.USER_INSURED_PRODUCT_DATA);
							} else {
								insuredProductData = dashboardService.getInsuredProductData(benefitPolicyList);
								addSession(UserDataInfo.USER_INSURED_PRODUCT_DATA, (Serializable) insuredProductData);
							}
							
							// 取得保障型的被保人資料
							//addAttribute("insuredProductData", insuredProductData);
							// 保障型商品數量
							//addAttribute("benefitPolicyListSize", benefitPolicyList.size());
							logger.debug("benefitPolicyListSize: {}", benefitPolicyList.size());
							logger.debug("insuredProductData: {}", MyJacksonUtil.object2Json(insuredProductData));
						}
						// 投資型保單
						if (getSession(UserDataInfo.USER_INVT_POLICY_LIST) != null) {
							invtPolicyList = (List<PolicyListVo>) getSession(UserDataInfo.USER_INVT_POLICY_LIST);
						} else {
							invtPolicyList = policyListService.getInvtPolicyList(userRocId);
							addSession(UserDataInfo.USER_INVT_POLICY_LIST, (Serializable) invtPolicyList);
						}
						
						if (invtPolicyList != null) {
							// 設定平均報酬率及帳戶價值資料
							portfolioService.setPortfolioData(invtPolicyList);
							for (PolicyListVo vo : invtPolicyList) {
								policyAcctValueTotal = policyAcctValueTotal.add(vo.getPolicyAcctValueNtd());
							}
							
							// 設定參考合計帳戶價值合計
							//addAttribute("policyAcctValueTotal", policyAcctValueTotal);
							//addAttribute("invtPolicyList", invtPolicyList);
						}
						
						// 取得我的資產與負債資料
						if (getSession(UserDataInfo.USER_POLICY_EXTRA) != null) {
							policyExtraVo = (PolicyExtraVo) getSession(UserDataInfo.USER_POLICY_EXTRA);
						} else {
							policyExtraVo = policyExtraService.getUserAllLoanData(userRocId);
							addSession(UserDataInfo.USER_POLICY_EXTRA, policyExtraVo);
						}
						//addAttribute("loanVo", policyExtraVo);

						DashboardResponse dashboardResponse = new DashboardResponse();
						 dashboardResponse.setBenefitPolicyListSize(benefitPolicyList.size());
						 dashboardResponse.setInvtPolicyList(invtPolicyList);
						 dashboardResponse.setPolicyAcctValueTotal(policyAcctValueTotal);
						 dashboardResponse.setPolicyExtraVo(policyExtraVo);
						addSession(UserDataInfo.USER_DASHBOARD_DATA, dashboardResponse);
					/*}*/
				}

				// 取得保障型的被保人資料
				addAttribute("insuredProductData", insuredProductData);
				
				Map<String, String> mapBase64 = null;
				if (insuredProductData != null) {
					Iterator<String> it = insuredProductData.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						String base64 = unicodeService.convertString2Unicode(key);
						if (!base64.equals("")) {
							if (mapBase64 == null) {
								mapBase64 = new HashMap<String, String>();
							}
							mapBase64.put(key, base64);
						}
					}
				}
				addAttribute("mapBase64", mapBase64);
				
				// 保障型商品數量
				addAttribute("benefitPolicyListSize", benefitPolicyListSize);
				logger.debug("benefitPolicyListSize: {}", benefitPolicyListSize);
				logger.debug("insuredProductData:", MyJacksonUtil.object2Json(insuredProductData));
				
				// 設定參考合計帳戶價值合計
				addAttribute("policyAcctValueTotal", policyAcctValueTotal);
				addAttribute("invtPolicyList", invtPolicyList);
				addAttribute("loanVo", policyExtraVo);
				
				// 20180424 CR16 系統登入成功/失敗發送電子郵件至要保人信箱 
				// 3.POP UP訊息告知最近三次登入時間及狀態
				if (ApConstants.isNotice) {
					if (getSession("auditLogList") != null) {
						List<AuditLogVo> auditLogList = (List<AuditLogVo>) getSession("auditLogList");
						if (auditLogList != null && auditLogList.size() > 0) {
							addAttribute("loginRecordList", auditLogList);
							addSession("auditLogList", null);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from dashboard: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "162");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		
		return "frontstage/dashboard";
	}
}