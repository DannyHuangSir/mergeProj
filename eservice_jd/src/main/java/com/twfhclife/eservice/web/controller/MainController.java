package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.ValidateCodeUtil;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.service.IParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class MainController extends BaseController {

	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@Autowired
	private IParameterService parameterService;

	/** 
	 * default index page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		// 若已經登入～直接到首頁
		if (getLoginUser() != null) {
			logger.debug("# has login session: redirect:dashboard");
			return "redirect:dashboard";
		}
		
		// 取得系統參數
		Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
		addAttribute(ApConstants.PAGE_WORDING, sysParamMap.get("PAGE_WORDING"));
		addAttribute(ApConstants.SYSTEM_CONSTANTS, sysParamMap.get("SYSTEM_CONSTANTS"));
		addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable)sysParamMap.get(ApConstants.SYSTEM_MSG_PARAMETER));
		Map<String, Map<String, ParameterVo>> sysParamMapMsg = new HashMap<String, Map<String,ParameterVo>>();
		sysParamMapMsg.put(ApConstants.SYSTEM_MSG_PARAMETER, sysParamMap.get(ApConstants.SYSTEM_MSG_PARAMETER));
		addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) sysParamMapMsg);
		
		// 設定驗證碼圖示
		ValidateCodeUtil vcUtil = new ValidateCodeUtil(101, 33, 4, 40);
		addSession(ApConstants.LOGIN_VALIDATE_CODE, vcUtil.getCode());
		addAttribute("validateImageBase64", vcUtil.imgToBase64String());
		
		return "login";
	}

	@GetMapping("/certificateRenew1")
	public String certificateRenew1() {
		logger.info("open frontstage/certificate-renew1.html");
		return "frontstage/certificate-renew1";
	}

	@GetMapping("/certificateRenew2")
	public String certificateRenew2() {
		logger.info("open frontstage/certificate-renew2.html");
		return "frontstage/certificate-renew2";
	}

	@GetMapping("/certificateRenewSuccess")
	public String certificateRenewSuccess() {
		logger.info("open frontstage/certificate-renew-success.html");
		return "frontstage/certificate-renew-success";
	}

	@GetMapping("/demo")
	public String demo() {
		logger.info("open frontstage/demo.html");
		return "frontstage/demo";
	}

	@GetMapping("/policyAutopay2")
	public String policyAutopay2() {
		logger.info("open frontstage/policy-autopay2.html");
		return "frontstage/policy-autopay2";
	}

	@GetMapping("/policyAutopay3")
	public String policyAutopay3() {
		logger.info("open frontstage/policy-autopay3.html");
		return "frontstage/policy-autopay3";
	}

	@GetMapping("/returnBonus2")
	public String returnBonus2() {
		logger.info("open frontstage/return-bonus2.html");
		return "frontstage/return-bonus2";
	}

	@GetMapping("/returnBonus3")
	public String returnBonus3() {
		logger.info("open frontstage/return-bonus3.html");
		return "frontstage/return-bonus3";
	}

	@GetMapping("/roiSearch")
	public String roiSearch() {
		logger.info("open frontstage/roi-search.html");
		return "frontstage/roi-search";
	}

	@GetMapping("/errorPage")
	public String errorPage() {
		logger.info("open frontstage/500.html");
		return "frontstage/500";
	}

}