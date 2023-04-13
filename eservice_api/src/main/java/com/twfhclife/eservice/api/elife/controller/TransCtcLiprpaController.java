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
import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaRequest;
import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLineacService;
import com.twfhclife.eservice.api.elife.service.ITransCtcLiprpaService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class TransCtcLiprpaController extends BaseController{
	private static final Logger logger = LogManager.getLogger(TransCtcLiprpaController.class);
	
	@Autowired
	private ITransCtcLiprpaService transCtcLiprpaService;
		
	@ApiRequest
	@PostMapping(value = "/getRevokeByLiprpaForInsuSeqNo", produces = { "application/json" })
	public ResponseEntity<?> getRevokeByLiprpaForInsuSeqNo(@Valid @RequestBody TransCtcLiprpaRequest req) {
		ApiResponseObj<TransCtcLiprpaResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLiprpaResponse resp = new TransCtcLiprpaResponse();
		try {
			List<TransCtcLiprpaVo> respList = transCtcLiprpaService.getRevokeByLiprpaForInsuSeqNo(req.getProdNo(), req.getPrpaInsuSeqNo());
			resp.setLiprpaList(respList);
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
