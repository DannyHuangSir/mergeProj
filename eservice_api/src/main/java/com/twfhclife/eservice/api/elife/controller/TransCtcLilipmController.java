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
import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmVo;
import com.twfhclife.eservice.api.elife.service.ITransCtcLilipmService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class TransCtcLilipmController  extends BaseController{	

	private static final Logger logger = LogManager.getLogger(TransCtcLilipmController.class);
	
	@Autowired
	private ITransCtcLilipmService transCtcLilipmService;	
	
	@ApiRequest
	@PostMapping(value = "/getCtcLilipm", produces = { "application/json" })
	public ResponseEntity<?> getCtcLilipm(@Valid @RequestBody TransCtcLilipmRequest req) {
		ApiResponseObj<TransCtcLilipmResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLilipmResponse resp = new TransCtcLilipmResponse();
		try {
			List<TransCtcLilipmVo>respList = transCtcLilipmService.getCtcLilipm(req.getLipmId());
			resp.setCtcLilipmListVo(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCtcLilipm: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}	
	
	@ApiRequest
	@PostMapping(value = "/getCtcLilipmDetail", produces = { "application/json" })
	public ResponseEntity<?> getCtcLilipmDetail(@Valid @RequestBody TransCtcLilipmRequest req) {
		ApiResponseObj<TransCtcLilipmResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLilipmResponse resp = new TransCtcLilipmResponse();
		try {
			List<TransCtcLilipmVo>respList = transCtcLilipmService.getCtcLilipmDetail(req.getPolicyNo());
			resp.setCtcLilipmListVo(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCtcLilipmDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getRevokeByLilipmForLipmInsuSeqNo", produces = { "application/json" })
	public ResponseEntity<?> getRevokeByLilipmForLipmInsuSeqNo(@Valid @RequestBody TransCtcLilipmRequest req) {
		ApiResponseObj<TransCtcLilipmResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLilipmResponse resp = new TransCtcLilipmResponse();
		try {
			List<TransCtcLilipmVo>respList = transCtcLilipmService.getRevokeByLilipmForLipmInsuSeqNo(req.getLipmInsuSeqNo());
			resp.setCtcLilipmListVo(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCtcLilipmDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getRevokeByLilipmForSeqNoAndAginRecCode", produces = { "application/json" })
	public ResponseEntity<?> getRevokeByLilipmForSeqNoAndAginRecCode(@Valid @RequestBody TransCtcLilipmRequest req) {
		ApiResponseObj<TransCtcLilipmResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLilipmResponse resp = new TransCtcLilipmResponse();
		try {
			List<TransCtcLilipmVo>respList = transCtcLilipmService.getRevokeByLilipmForSeqNoAndAginRecCode(req.getLipmInsuSeqNo());
			resp.setCtcLilipmListVo(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCtcLilipmDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getOnlineTrialDetail", produces = { "application/json" })
	public ResponseEntity<?> getOnlineTrialDetail(@Valid @RequestBody TransCtcLilipmRequest req) {
		ApiResponseObj<TransCtcLilipmResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcLilipmResponse resp = new TransCtcLilipmResponse();
		try {
			List<TransCtcLilipmVo>respList = transCtcLilipmService.getOnlineTrialDetail(req.getLipmInsuSeqNo());
			resp.setCtcLilipmListVo(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getCtcLilipmDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
}
