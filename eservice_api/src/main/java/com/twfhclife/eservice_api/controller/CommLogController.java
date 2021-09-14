package com.twfhclife.eservice_api.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice_api.service.ICommLogService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.CommLogRequest;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 儲存email/sms發送紀錄.
 * 
 * @author all
 */
@RestController
public class CommLogController extends BaseController {

	private static final Logger logger = LogManager.getLogger(CommLogController.class);
	
	@Autowired
	private ICommLogService commLogService;

	/**
	 * 儲存email/sms發送紀錄.
	 * 
	 * @param req CommLogRequest
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/commLog/add", produces = { "application/json" })
	public ResponseEntity<?> addCommLog(@Valid @RequestBody CommLogRequest req) {
		ApiResponseObj apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		
		try {
			if (!"email".equalsIgnoreCase(req.getCommType()) && !"sms".equalsIgnoreCase(req.getCommType())) {
				throw new Exception("commType must be email or sms");
			}
			commLogService.addCommLog(req);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to addCommLog: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}