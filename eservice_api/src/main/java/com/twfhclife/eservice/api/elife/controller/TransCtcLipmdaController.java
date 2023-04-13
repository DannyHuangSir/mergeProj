package com.twfhclife.eservice.api.elife.controller;

import java.util.List;

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

import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmRequest;
import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaRequest;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLilipmService;
import com.twfhclife.eservice.api.elife.service.ITransCtcLipmdaService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class TransCtcLipmdaController extends BaseController{
	private static final Logger logger = LogManager.getLogger(TransCtcLipmdaController.class);
	
	@Autowired
	private ITransCtcLipmdaService transCtcLipmdaService;	
	
	@ApiRequest
	@PostMapping(value = "/getCtcLipmda", produces = { "application/json" })
	public ResponseEntity<?> getCtcLipmda(@Valid @RequestBody TransCtcLipmdaRequest req) {
		ApiResponseObj<TransCtcLipmdaResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLipmdaResponse resp = new TransCtcLipmdaResponse();
		try {
			List<TransCtcLipmdaVo> respList = transCtcLipmdaService.getCtcLipmda(req.getUserId());
			resp.setLipmdaList(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getLilipm: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
}
