package com.twfhclife.eservice.onlineChange.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.OnlineTrialVo;
import com.twfhclife.eservice.onlineChange.model.TransOnlineTrialVo;
import com.twfhclife.eservice.onlineChange.service.ITransOnlineTrialService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeNoteUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;

@Controller
public class TransOnlineTrialController extends BaseUserDataController {
	
	private static final Logger logger = LogManager.getLogger(TransOnlineTrialController.class);

	@Autowired
	private ITransOnlineTrialService transOnlineTrialService;
	
	/**
	 * 取得減額繳清保險畫面資料
	 * @return
	 */
	@RequestLog
	@GetMapping("/onlineTrial1")
	public String getOnlineTrial1(RedirectAttributes redirectAttributes) {
		try {
			/**
			 * 1.確認功能是否可以使用
			 */
			if (!checkCanUseOnlineChange()) {
				/* addSystemError("目前無法使用此功能，請臨櫃申請線上服務。"); */
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				redirectAttributes.addFlashAttribute("errorMessage", message);
				return "redirect:apply1";
			}
			/**
			 * 2.呼叫CTC查詢資料
			 */
			
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<OnlineTrialVo> transOnlineTrialList =  transOnlineTrialService.getPolicyDetail(userRocId ,userId , TransTypeUtil.ONLINE_TRIAL);
			transOnlineTrialList = transOnlineTrialList.stream().sorted(Comparator.comparing(OnlineTrialVo::getLipmInsuNo))
					.collect(Collectors.toList());
			transOnlineTrialList = transOnlineTrialList.stream().sorted(Comparator.comparing(OnlineTrialVo::getApplyLockedFlag))
					.collect(Collectors.toList());
			addAttribute("policyList", transOnlineTrialList);

		} catch (Exception e) {
			logger.error("Unable to init from onlineTrial: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/onlineTrial/onlineTrial1";
	}
	
	@RequestLog
	@PostMapping("/onlineTrial2")
	public String getOnlineTrial2(OnlineTrialVo onlineTrialVo ) {
		
		TransOnlineTrialVo vo = transOnlineTrialService.getOnlineTrialDetail(onlineTrialVo.getLipmInsuNo());

		if(vo == null) {
			return getOnlineTrial1(null);
		}
		if(!StringUtils.isEmpty(vo.getRefundMsg())) {
			addAttribute("refundErrorMessage", getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
					OnlineChangeNoteUtil.ONLINE_TRIAL_CODE));
		}
	
		addAttribute("transOnlineTrialVo", vo);
		
		return "frontstage/onlineChange/onlineTrial/onlineTrial2";
	}	
}
