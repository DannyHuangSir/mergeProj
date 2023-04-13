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

import com.twfhclife.eservice.api.elife.domain.CspData;
import com.twfhclife.eservice.api.elife.domain.TransCsp002DataRequest;
import com.twfhclife.eservice.api.elife.domain.TransCsp002DataResponse;
import com.twfhclife.eservice.api.elife.domain.TransCspApiUtilRequest;
import com.twfhclife.eservice.api.elife.domain.TransCspApiUtilResponse;
import com.twfhclife.eservice.api.elife.domain.TransOnlineTrialVo;
import com.twfhclife.eservice.api.elife.service.ITransCspApiUtilService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class TransCspApiUtilController  extends BaseController{
private static final Logger logger = LogManager.getLogger(TransCspApiUtilController.class);
	
	@Autowired
	private ITransCspApiUtilService transCspApiUtilService;
		
	@ApiRequest
	@PostMapping(value = "/getCsp001Detail", produces = { "application/json" })
	public ResponseEntity<?> getCsp001Detail(@Valid @RequestBody TransCspApiUtilRequest req) {
		ApiResponseObj<TransCspApiUtilResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCspApiUtilResponse resp = new TransCspApiUtilResponse();
		try {
			CspData cspData= transCspApiUtilService.getCsp001Detail(req);
			resp.setCspData(cspData);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCsp001Detail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getCsp002Detail", produces = { "application/json" })
	public ResponseEntity<?> getCsp002Detail(@Valid @RequestBody TransCsp002DataRequest req) {
		ApiResponseObj<TransCsp002DataResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCsp002DataResponse resp = new TransCsp002DataResponse();
		try {
			TransOnlineTrialVo transOnlineTrialVo= transCspApiUtilService.getCsp0021Detail(req);
			resp.setTransOnlineTrialVo(transOnlineTrialVo);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCsp002Detail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}
