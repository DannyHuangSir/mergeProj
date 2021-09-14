package com.twfhclife.eservice_api.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.api.adm.model.DepartmentVo;
import com.twfhclife.eservice.api.adm.service.IFunctionDailyUsageService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.FunctionUsageAdd;
import com.twfhclife.generic.domain.ReturnHeader;

@Controller
@EnableAutoConfiguration
public class MainController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@Autowired
	private IFunctionDailyUsageService functionDailyUsageService;

	/** 
	 * default index page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/", "/index", "/test" }, method = RequestMethod.GET)
	public String index(Model model) {
		logger.info("#Open test page#");
		return "redirect:/testpage.html";
	}

	@ApiRequest
	@PostMapping(value = "/function/usage/add", produces = { "application/json" })
	public ResponseEntity<?> addFunctionUsage(@RequestBody FunctionUsageAdd req) {
		ApiResponseObj<Object> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			logger.info("addFunctionUsage for functionId:"+req.getFunctionId()+",systemId="+req.getSystemId());
			// calling service here
			functionDailyUsageService.addFunctionUsageCount(req.getFunctionId(), req.getSystemId());
			
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");

		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			response.setReturnHeader(returnHeader);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
