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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

	@PostMapping("/getLevelStates")
	public  ResponseEntity<ResponseObj> getLevelStates() {
		synchronized(this){
			try {
				processSuccess(optionService.getLevelStates());
			} catch (Exception e) {
				logger.error("Unable to getPolicyTyps: {}", ExceptionUtils.getStackTrace(e));
				processSystemError();
			}
			return processResponseEntity();
		}
	}

	@PostMapping("/getPayModeList")
	public ResponseEntity<ResponseObj> getPayModeList() {
		try {
			List<Map<String, Object>> regionList = optionService.getPayModeList();
			processSuccess(regionList);
		} catch (Exception e) {
			logger.error("Unable to getPayModeList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@PostMapping("/getTransactionCodeList")
	public ResponseEntity<ResponseObj> getTransactionCodeList() {
		try {
			processSuccess(optionService.getTransactionCodeList());
		} catch (Exception e) {
			logger.error("Unable to getTransactionCodeList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

}
