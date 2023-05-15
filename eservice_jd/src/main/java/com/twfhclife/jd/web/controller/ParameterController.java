package com.twfhclife.jd.web.controller;

import com.twfhclife.jd.controller.BaseMvcController;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.web.domain.ResponseObj;
import com.twfhclife.jd.web.service.IParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 參數資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class ParameterController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(ParameterController.class);
	
	private static final String SYSTEM_ID = "eservice_jd";

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
