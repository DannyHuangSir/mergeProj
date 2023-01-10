package com.twfhclife.eservice.controller;

import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.web.domain.ResponseObj;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BaseMvcController {
	
	private Map<String, Object> dataResult = new HashMap<>();

	private ResponseObj responseObj= new ResponseObj();

	public ResponseObj getResponseObj() {
		return responseObj;
	}
	
	public void setResponseObj(String result, String resultMsg, Object resultData) {
		responseObj.setResult(result);
		responseObj.setResultMsg(resultMsg);
		responseObj.setResultData(resultData);
	}
	
	public void processSuccess(Object resultData) {
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultMsg("");
		responseObj.setResultData(resultData);
	}
	
	public void processSuccessMsg(String successMsg) {
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultMsg(successMsg);
	}
	
	public void processSystemError() {
		responseObj.setResult(ResponseObj.ERROR);
		responseObj.setResultMsg(ApConstants.SYSTEM_ERROR);
	}
	
	public void processError(String errorMsg) {
		responseObj.setResult(ResponseObj.ERROR);
		responseObj.setResultMsg(errorMsg);
	}
	
	public void addJsonData(String key, Object data) {
		dataResult.put(key, data);
	}

	public Map<String, Object> getDataResult() {
		return dataResult;
	}
	
	public ResponseEntity<ResponseObj> processResponseEntity() {
		if (responseObj == null) {
			responseObj = new ResponseObj();
			responseObj.setResult(ResponseObj.SUCCESS);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}
}
