package com.twfhclife.generic.aspect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.twfhclife.eservice.dashboard.model.EstatmentVo;
import com.twfhclife.eservice.dashboard.service.IApplyProgressService;
import com.twfhclife.eservice.dashboard.service.IEstatmentService;
import com.twfhclife.eservice.dashboard.service.IPaymentReminderService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import com.twfhclife.generic.api_model.PolicyListResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.IUnicodeService;

@Aspect
@Configuration
public class UserDataAspect extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(UserDataAspect.class);

	/**
	 * 線上申請進度服務
	 */
	@Autowired
	private IApplyProgressService applyProgressService;

	/**
	 * 繳費提醒服務
	 */
	@Autowired
	private IPaymentReminderService paymentReminderService;

	/**
	 * 我的通知服務
	 */
	@Autowired
	private IEstatmentService estatmentService;
	
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private IRegisterUserService registerUserService;
	
	@Autowired
	private IUnicodeService unicodeService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Pointcut("@within(org.springframework.stereotype.Controller)")
	public void controllerMethodPointcut() {
	}

	@Around("controllerMethodPointcut()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Signature signature =  pjp.getSignature();
		
		// 只有頁面轉換時才需要處理，排除 ajax 取得 ResponseEntity
		if ("java.lang.String".equals(((MethodSignature) signature).getReturnType().getTypeName())) {
			String loginUserType = getLoginUserType();
			if (!StringUtils.isEmpty(loginUserType)) {
				LilipmVo lilipmVo = null;
				switch (loginUserType) {
				case "normal":
					// 一般用戶
					break;
				case "member":
					// 保戶用戶
					String userRocId = getUserRocId();
					
					setUserDataInfo();
					setNoticeBoardList(userRocId);
					setProcessingList(userRocId);
					setCompleteList(userRocId);
					setPaymentNotificationList(userRocId);
					break;
				}
			}
		}
		return pjp.proceed();
	}

	/**
	 * 設定事件通知資料.
	 */
	@SuppressWarnings("unchecked")
	private void setNoticeBoardList(String rocId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			List<EstatmentVo> noticeBoardList = new ArrayList<>();
			
			// 查詢從session中取得資料
			Object noticeBoardListObj = getSession(UserDataInfo.USER_NOTICE_BOARD_LIST);
			if (noticeBoardListObj != null) {
				noticeBoardList = (List<EstatmentVo>) noticeBoardListObj;
			} else {
				if (!StringUtils.isEmpty(rocId)) {
					EstatmentVo estatmentVo = new EstatmentVo();
					estatmentVo.setRocId(rocId);
					noticeBoardList = estatmentService.getNoticeBoardList(estatmentVo);
					addSession(UserDataInfo.USER_NOTICE_BOARD_LIST, (Serializable) noticeBoardList);
				}
			}
			
			BigDecimal notificationNotRead = BigDecimal.ZERO;  // 未讀數量
			List<String> noticeMonthList = new LinkedList<>(); // 通知月份
			if (!CollectionUtils.isEmpty(noticeBoardList)) {
				for (EstatmentVo vo : noticeBoardList) {
					String noticeMonth = sdf.format(vo.getCreateDate());
					if (!noticeMonthList.contains(noticeMonth)) {
						noticeMonthList.add(noticeMonth);
					}
					
					if (vo.getStatus().equals(BigDecimal.ZERO)) {
						notificationNotRead = notificationNotRead.add(BigDecimal.ONE);
					}
				}
			}
			
			addAttribute("notificationNotRead", notificationNotRead);
			addAttribute("noticeBoardList", noticeBoardList);
			addAttribute("noticeMonthList", noticeMonthList);
		} catch (Exception e) {
			logger.error("Unable to getNoticeBoardList: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定案件申請中資料.
	 * 
	 * @param userRocId 用戶證號
	 */
	@SuppressWarnings("unchecked")
	private void setProcessingList(String userRocId) {
		try {
			List<TransVo> processingList = new ArrayList<>();
			
			// 查詢從session中取得資料
			Object processingListObj = getSession(UserDataInfo.USER_PROCESSING_LIST);
			if (processingListObj != null) {
				processingList = (List<TransVo>) processingListObj;
			} else {
				if (!StringUtils.isEmpty(userRocId)) {
					processingList = applyProgressService.getApplyProgressList(userRocId);
					addSession(UserDataInfo.USER_PROCESSING_LIST, (Serializable) processingList);
				}
			}
			
			addAttribute("processingList", processingList);
		} catch (Exception e) {
			logger.error("Unable to getChangeHistoryList: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定案件已完成資料.
	 * 
	 * @param userRocId 用戶證號
	 */
	@SuppressWarnings("unchecked")
	private void setCompleteList(String userRocId) {
		try {
			List<TransVo> completeList = new ArrayList<>();
			
			// 查詢從session中取得資料
			Object completeListObj = getSession(UserDataInfo.USER_COMPLETE_LIST);
			if (completeListObj != null) {
				completeList = (List<TransVo>) completeListObj;
			} else {
				if (!StringUtils.isEmpty(userRocId)) {
					completeList = applyProgressService.getChangeHistoryList(userRocId,
							OnlineChangeUtil.TRANS_STATUS_COMPLETE);
					addSession(UserDataInfo.USER_COMPLETE_LIST, (Serializable) completeList);
				}
			}
			
			addAttribute("completeList", completeList);
		} catch (Exception e) {
			logger.error("Unable to getChangeHistoryList: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定繳費通知資料.
	 * 
	 * @param userRocId 用戶證號
	 */
	@SuppressWarnings("unchecked")
	private void setPaymentNotificationList(String userRocId) {
		try {
			List<PolicyExtraVo> paymentReminderList = new ArrayList<>();
			BigDecimal totalUnpaidAmount = BigDecimal.ZERO; // 未繳總金額
			
			// 查詢從session中取得資料
			Object paymentReminderListObj = getSession(UserDataInfo.USER_PAYMENT_REMINDER_LIST);
			if (paymentReminderListObj != null) {
				paymentReminderList = (List<PolicyExtraVo>) paymentReminderListObj;
			} else {
				if (!StringUtils.isEmpty(userRocId)) {
					paymentReminderList = paymentReminderService.getPaymentReminderList(userRocId);
					addSession(UserDataInfo.USER_PAYMENT_REMINDER_LIST, (Serializable) paymentReminderList);
				}
			}
			
			for (PolicyExtraVo vo : paymentReminderList) {
				if (vo.getAplAmount() != null) {
					totalUnpaidAmount = totalUnpaidAmount.add(vo.getAplAmount());
				} else {
					BigDecimal unpaidAmount = BigDecimal.ZERO;
					if (vo.getPolicyVo() != null && vo.getPolicyVo().getUnpaidAmount() != null) {
						unpaidAmount = vo.getPolicyVo().getUnpaidAmount();
					}
					totalUnpaidAmount = totalUnpaidAmount.add(unpaidAmount);
				}
			}
			
			addAttribute("paymentReminderList", paymentReminderList);
			addAttribute("totalUnpaidAmount", totalUnpaidAmount);
		} catch (Exception e) {
			logger.error("Unable to getPaymentNotification: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定使用者保單相關資訊.
	 */
	private void setUserDataInfo() {
		try {
			UserDataInfo userDataInfo = null;
			if (getSession("userDataInfo") == null) {
				userDataInfo = new UserDataInfo();
				setProductNameMap(userDataInfo);
				setMainInsuredNameMap(userDataInfo);
				addSession("userDataInfo", userDataInfo);
			} else {
				userDataInfo = (UserDataInfo) getSession("userDataInfo");
			}
			addAttribute("userInfo", userDataInfo);
		} catch (Exception e) {
			logger.error("Unable to setUserDataInfo: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定使用者的保單名稱清單.
	 */
	@SuppressWarnings("unchecked")
	private void setProductNameMap(UserDataInfo userDataInfo) {
		try {
			Object usrProdObj = getSession(UserDataInfo.USER_PRODUCT_NAME_LIST);
			if (usrProdObj != null) {
				List<Map<String, String>> userPolicyNoList = (List<Map<String, String>>) usrProdObj;
				if (userPolicyNoList != null) {
					for (Map<String, String> map : userPolicyNoList) {
						userDataInfo.getProductNameMap().put(map.get("POLICY_NO"), map.get("PRODUCT_NAME"));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to setProductNameMap: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定使用者的保單主約被保險人名稱清單.
	 */
	@SuppressWarnings("unchecked")
	private void setMainInsuredNameMap(UserDataInfo userDataInfo) {
		try {
			Object usrMainInsuredNameObj = getSession(UserDataInfo.USER_MAIN_INSURED_NAME_LIST);
			if (usrMainInsuredNameObj != null) {
				List<Map<String, String>> usrMainInsuredNameList = (List<Map<String, String>>) usrMainInsuredNameObj;
				if (usrMainInsuredNameList != null) {
					for (Map<String, String> map : usrMainInsuredNameList) {
						userDataInfo.getMainInsuredNameMap().put(map.get("POLICY_NO"), map.get("MAIN_INSURED_NAME"));
						if (map.get("MAIN_INSURED_NAME") != null
								&& !StringUtils.isEmpty(map.get("MAIN_INSURED_NAME"))) {
							userDataInfo.getMainInsuredNameBase64Map().put(map.get("POLICY_NO"),
									unicodeService.convertString2Unicode(map.get("MAIN_INSURED_NAME")));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to setMainInsuredNameMap: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 設定使用者的身份類型.
	 */
	private String getLoginUserType() {
		String loginUserType = getUserType();
		if (!StringUtils.isEmpty(loginUserType)) {
			addAttribute("loginUserType", loginUserType);
			
			// 是否顯示用戶資訊的通知欄區域
			switch (loginUserType) {
			case "normal":
				// 一般用戶
				addAttribute("showUserDataInfo", false);
				addAttribute("isPartnerUser", false);
				break;
			case "member":
				// 保戶用戶
				addAttribute("showUserDataInfo", true);
				addAttribute("isPartnerUser", false);
				break;
			case "agent":
				// 保經代
				addAttribute("showUserDataInfo", false);
				addAttribute("isPartnerUser", true);
				break;
			case "admin":
				// 內部人員
				addAttribute("showUserDataInfo", true);
				addAttribute("isPartnerUser", true);
				break;
			default:
				addAttribute("showUserDataInfo", false);
				addAttribute("isPartnerUser", false);
			}
		}
		return loginUserType;
	}
}