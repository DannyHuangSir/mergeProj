package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController extends BaseController {

	@Autowired
	private ICaseService caseService;
	@GetMapping(value = { "/", "index", "/dashboard" })
	public String dashboard() {
		addAttribute("caseList", caseService.getCaseList(getLoginUser()));
		return "frontstage/dashboard";
	}
}