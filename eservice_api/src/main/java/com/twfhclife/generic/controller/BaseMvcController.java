package com.twfhclife.generic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.twfhclife.generic.constants.ErrorMsg;
import com.twfhclife.generic.domain.ResponseObj;
import com.twfhclife.generic.utils.ApConstants;

/**
 * Provide common controller some method.
 * <p>
 * Add createSuccessResp() and createErrorResp() method by tcMarcus at 2018/03/12 
 * Add handlingDataNotFound() method by tcMarcus at 2018/03/14 
 * Add doExceptionHandling() and findMethodName method by tcMarcus at 2018/03/15
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class BaseMvcController {

	private Logger logger = LoggerFactory.getLogger(BaseMvcController.class);

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

	/**
	 * Create response after invoking API without any error.
	 * 
	 * @param resultData
	 * @return ResponseEntity<ResponseObj>
	 */
	protected ResponseEntity<ResponseObj> createSuccessResp(Object resultData) {

		setResponseObj(ResponseObj.SUCCESS, "", resultData);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	/**
	 * Create response when API occurs exception.
	 * 
	 * @param msgArgs
	 * @param resultData
	 * @return ResponseEntity<ResponseObj>
	 */
	protected ResponseEntity<ResponseObj> createErrorResp(String resultMsg, Object resultData) {

		setResponseObj(ResponseObj.ERROR, resultMsg, resultData);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	/**
	 * Handling data not found response.
	 * 
	 * @param list
	 * @return ResponseEntity<ResponseObj>
	 */
	protected ResponseEntity<ResponseObj> handlingDataNotFound(List<?> list) {

		ResponseEntity<ResponseObj> response = null;

		if (list == null || list.isEmpty()) {

			String errorMessage = ErrorMsg.DATA_NOT_FOUND;
			response = createErrorResp(errorMessage, list);
		}

		return response;
	}

	/**
	 * Handling error message from the specified exception.
	 *
	 * @param e          The specified exception.
	 * @param methodName The name of the method that catches this exception.
	 * @return ResponseEntity<ResponseObj>.
	 */
	protected ResponseEntity<ResponseObj> doExceptionHandling(Exception e) {

		logger.error(e.getMessage(), e);

		String errorMessage = "";

		String exceptionClassName = e.getClass() != null ? e.getClass().getSimpleName() : e.toString();

		String methodName = findMethodName(e.getCause());

		if (e.getCause() != null) {
			String cause = String.format("%s. Message: %s",
										 e.getCause().getClass().getSimpleName(),
										 e.getCause().getMessage());

			errorMessage = String.format("%s. %s caught on %s with root cause: %s",
										 e.getMessage(),
										 exceptionClassName,
										 methodName,
										 cause);
		} else {
			errorMessage = String.format("%s. %s caught on %s",
										 e.getMessage(),
										 exceptionClassName,
										 methodName);
		}

		return createErrorResp(errorMessage, null);
	}

	/**
	 * Finds the name of the method where the specified throwable object throws. 
	 * The patter of the returned string will be [class_name].[method_name].
	 *
	 * @param cause The specified throwable object.
	 * @return The name of the method where the specified throwable object throws.
	 *         The patter of the returned string will be [class_name].[method_name].
	 */
	private String findMethodName(final Throwable cause) {

		String fullMethodName = "";

		try {
			Throwable rootCause = cause;

			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				rootCause = rootCause.getCause();
			}

			String className = rootCause.getStackTrace()[0].getClassName();
			String methodName = rootCause.getStackTrace()[0].getMethodName();

			fullMethodName = className + "." + methodName;
		} catch (Throwable e) {
			fullMethodName = ErrorMsg.METHOD_NOT_SPECIFIED;
		}

		return fullMethodName;
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
