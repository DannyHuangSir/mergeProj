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

import com.twfhclife.eservice.api.elife.domain.LicohilRequest;
import com.twfhclife.eservice.api.elife.domain.LicohilResponse;
import com.twfhclife.eservice.api.elife.domain.LicohilVo;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailResponse;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectUtilRequest;
import com.twfhclife.eservice.api.elife.service.ITransCtcSelectUtilService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
@RestController
public class TransCtcSelectUtilController extends BaseController{
	
	private static final Logger logger = LogManager.getLogger(TransCtcSelectUtilController.class);
	
	@Autowired
	private ITransCtcSelectUtilService transCtcSelectUtilService;	
		
	@ApiRequest
	@PostMapping(value = "/getTransCtcSelectDataByLipmId", produces = { "application/json" })
	public ResponseEntity<?> getTransCtcSelectDataByLipmId(@Valid @RequestBody TransCtcSelectUtilRequest req) {
		ApiResponseObj<TransCtcSelectDataResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcSelectDataResponse resp = new TransCtcSelectDataResponse();
		try {
			List<TransCtcSelectDataVo> respList = transCtcSelectUtilService.getTransCtcSelectDataByLipmId(req.getLipmId());
			resp.setSelectDataList(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getTransCtcSelectDataByLipmId: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getTransCtcSelectDetailByLipmInsuSeqNo", produces = { "application/json" })
	public ResponseEntity<?> getTransCtcSelectDetailByLipmInsuSeqNo(@Valid @RequestBody TransCtcSelectUtilRequest req) {
		ApiResponseObj<TransCtcSelectDetailResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcSelectDetailResponse resp = new TransCtcSelectDetailResponse();
		try {
			List<TransCtcSelectDetailVo> respList = transCtcSelectUtilService.getTransCtcSelectDetailByLipmInsuSeqNo(req.getLipmInsuSeqNo());
			resp.setSelectDetailList(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getTransCtcSelectDetailByLipmInsuSeqNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getTransCtcSelectDataAddCode", produces = { "application/json" })
	public ResponseEntity<?> getTransCtcSelectDataAddCode(@Valid @RequestBody TransCtcSelectUtilRequest req) {
		ApiResponseObj<TransCtcSelectDataAddCodeResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransCtcSelectDataAddCodeResponse resp = new TransCtcSelectDataAddCodeResponse();
		try {
			List<TransCtcSelectDataAddCodeVo> respList = transCtcSelectUtilService.getTransCtcSelectDataAddCode(req.getLipmId());
			resp.setDataAddCodeList(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getTransCtcSelectUtilByLipmId: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getPolicyDataByRocId", produces = { "application/json" })
	public ResponseEntity<?> getPolicyDataByRocId(@Valid @RequestBody PolicyDetailRequest req) {
		ApiResponseObj<PolicyDetailResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyDetailResponse resp = new PolicyDetailResponse();
		try {
			List<PolicyDetailVo> respList = transCtcSelectUtilService.getPolicyDataByRocId(req);
			resp.setPolicyDetailList(respList);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyDataByRocId: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getLicohiByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getLicohiByPolicyNo(@Valid @RequestBody PolicyDetailRequest req) {
		ApiResponseObj<LicohilResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		LicohilResponse resp = new LicohilResponse();
		try {
			List<LicohilVo> licohilVo = transCtcSelectUtilService.getLicohiByPolicyNo(req);
			resp.setLicohilVo(licohilVo);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getIindivdualDetailByPolicyNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/getLilipmByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getLilipmByPolicyNo(@Valid @RequestBody PolicyDetailRequest req) {
		ApiResponseObj<LicohilResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		LicohilResponse resp = new LicohilResponse();
		try {
			List<LicohilVo> licohilVo = transCtcSelectUtilService.getLilipmByPolicyNo(req);
			resp.setLicohilVo(licohilVo);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getIindivdualDetailByPolicyNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}
