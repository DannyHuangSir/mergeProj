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

	@GetMapping("/errorPage")
	public String errorPage() {
		logger.info("open frontstage/500.html");
		return "frontstage/500";
	}
}