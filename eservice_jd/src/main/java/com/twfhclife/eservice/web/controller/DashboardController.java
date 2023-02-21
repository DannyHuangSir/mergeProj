package com.twfhclife.eservice.web.controller;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DashboardController extends BaseController {

	@Autowired
	private ICaseService caseService;
	@GetMapping(value = { "/", "index", "/dashboard" })
	public String dashboard() {
		return "frontstage/dashboard";
	}

	@PostMapping(value = { "/personalCaseList" })
	@ResponseBody
	public ResponseObj personalCaseList() {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getCaseList(getLoginUser()));
		return responseObj;
	}
}