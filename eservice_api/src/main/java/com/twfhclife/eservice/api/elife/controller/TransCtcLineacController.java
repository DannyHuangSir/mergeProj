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

import com.twfhclife.eservice.api.elife.domain.TransCtcLineacRequest;
import com.twfhclife.eservice.api.elife.domain.TransCtcLineacResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLineacVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaRequest;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLilipmService;
import com.twfhclife.eservice.api.elife.service.ITransCtcLineacService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
@RestController
public class TransCtcLineacController extends BaseController{
	
	private static final Logger logger = LogManager.getLogger(TransCtcLineacController.class);
	
	@Autowired
	private ITransCtcLineacService transCtcLineacService;
	

	
	@ApiRequest
	@PostMapping(value = "/getRevokeByLineacForNeacInsuNo", produces = { "application/json" })
	public ResponseEntity<?> getRevokeByLineacForNeacInsuNo(@Valid @RequestBody TransCtcLineacRequest req) {
		ApiResponseObj<TransCtcLineacResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLineacResponse resp = new TransCtcLineacResponse();
		try {
			List<TransCtcLineacVo> respList = transCtcLineacService.getRevokeByLineacForNeacInsuNo(req.getInsuNo());
			resp.setLineacList(respList);
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
