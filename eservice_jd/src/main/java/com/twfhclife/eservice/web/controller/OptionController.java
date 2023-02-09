package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseMvcController;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.service.IOptionService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 選單資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class OptionController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(OptionController.class);

	@Autowired
	private IOptionService optionService;

	@PostMapping("/getPolicyTyps")
	public  ResponseEntity<ResponseObj> getPolicyTyps() {
		synchronized(this){
		try {
			processSuccess(optionService.getPolicyTyps());
		} catch (Exception e) {
			logger.error("Unable to getPolicyTyps: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
		}
	}
}
