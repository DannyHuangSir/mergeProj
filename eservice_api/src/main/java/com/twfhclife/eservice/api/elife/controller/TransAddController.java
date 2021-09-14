package com.twfhclife.eservice.api.elife.controller;

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

import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 送出線上申請服務.
 * 
 * @author all
 */
@RestController
public class TransAddController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TransAddController.class);
	
	@Autowired
	private ITransAddService transAddService;

	/**
	 * 送出線上申請.
	 * 
	 * @param req TransAddRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-018", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.onlineChange.dao.TransDao.getTransNum",
					execMethod = "送出線上申請"
				)
			}))
	@PostMapping(value = "/addTransRequest", produces = { "application/json;charset=UTF-8" })
	public ResponseEntity<?> addTransRequest(@Valid @RequestBody TransAddRequest req) {
		ApiResponseObj<TransAddResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransAddResponse resp = new TransAddResponse();
		try {
			resp = transAddService.addTransRequest(req);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to addTransRequest: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}