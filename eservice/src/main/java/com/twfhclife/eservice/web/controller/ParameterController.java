package com.twfhclife.eservice.web.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 參數資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class ParameterController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(ParameterController.class);
	
	private static final String SYSTEM_ID = "eservice";

	@Autowired
	private IParameterService parameterService;

	@PostMapping("/getParameterList")
	public ResponseEntity<ResponseObj> getParameterList(@RequestParam("categoryCode") String categoryCode) {
		logger.info("call /getParameterList with categoryCode: {}", categoryCode);
		try {
			List<Map<String, Object>> parameterList = parameterService.getParameterList(SYSTEM_ID, categoryCode);
			this.setResponseObj(ResponseObj.SUCCESS, null, parameterList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR_PARAMETER, null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
}
