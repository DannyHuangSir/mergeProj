package com.twfhclife.generic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.generic.util.ApConstants;

public class BaseMvcController {
	private ResponseObj responseObj;

	public ResponseObj getResponseObj() {
		return responseObj;
	}
	
	public void setResponseObj(String result, String resultMsg, Object resultData) {
		responseObj = new ResponseObj();
		responseObj.setResult(result);
		responseObj.setResultMsg(resultMsg);
		responseObj.setResultData(resultData);
	}
	
	public void processSuccess(Object resultData) {
		responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(resultData);
	}
	
	public void processSuccessMsg(String successMsg) {
		responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultMsg(successMsg);
	}
	
	public void processSystemError() {
		responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.ERROR);
		responseObj.setResultMsg(ApConstants.SYSTEM_ERROR);
	}
	
	public void processError(String errorMsg) {
		responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.ERROR);
		responseObj.setResultMsg(errorMsg);
	}
	
	public ResponseEntity<ResponseObj> processResponseEntity() {
		if (responseObj == null) {
			responseObj = new ResponseObj();
			responseObj.setResult(ResponseObj.SUCCESS);
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}
}
