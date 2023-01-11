package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController extends BaseController {


	@GetMapping(value = { "/", "index", "/dashboard" })
	public String dashboard() {
		return "frontstage/dashboard";
	}
}