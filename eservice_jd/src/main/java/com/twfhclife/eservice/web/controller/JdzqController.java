package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.ValidateCodeUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.eservice.web.service.IPolicyService;
import com.twfhclife.eservice.web.service.IRegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

@Controller
public class JdzqController extends BaseController {

	private static final Logger logger = LogManager.getLogger(JdzqController.class);

	@Autowired
	private IPolicyService policyService;

	@GetMapping("/policyQuery")
	public String policyInquiry() {
		addAttribute("policyList", policyService.getPolicyList());
		return "frontstage/jdzq/policyQuery/policy-query";
	}

	@GetMapping("/protectQuery")
	public String protectQuery() {
		return "frontstage/jdzq/protectQuery/protect-query";
	}

	@GetMapping("/caseQuery")
	public String caseQuery() {
		return "frontstage/jdzq/caseQuery/case-query";
	}
}