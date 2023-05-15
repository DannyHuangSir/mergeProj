package com.twfhclife.jd.web.controller;

import com.twfhclife.jd.controller.BaseController;
import com.twfhclife.jd.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController extends BaseController {

	@Autowired
	private ICaseService caseService;
	@GetMapping(value = { "/", "index", "/dashboard" })
	public String dashboard() {
		return "frontstage/dashboard";
	}

}