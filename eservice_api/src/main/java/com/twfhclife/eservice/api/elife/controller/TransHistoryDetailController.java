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

import com.twfhclife.eservice.api.elife.domain.TransHistoryDetailRequest;
import com.twfhclife.eservice.api.elife.domain.TransHistoryDetailResponse;
import com.twfhclife.eservice.api.elife.service.ITransHistoryDetailService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 查詢線上申請明細紀錄.
 * 
 * @author all
 */
@RestController
public class TransHistoryDetailController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TransHistoryDetailController.class);
	
	@Autowired
	private ITransHistoryDetailService transHistoryDetailService;

	/**
	 * 查詢線上申請明細.
	 * 
	 * @param req TransHistoryDetailRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-017", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.onlineChange.dao.TransDao.findByTransNum",
					execMethod = "查詢線上申請明細",
					sqlParams = { 
						@SqlParam(requestParamkey = "transNums", sqlParamkey = "transNum")
					}
				)
			}))
	@PostMapping(value = "/getTransHistoryDetail", produces = { "application/json" })
	public ResponseEntity<?> getTransHistoryDetail(@Valid @RequestBody TransHistoryDetailRequest req) {
		ApiResponseObj<TransHistoryDetailResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransHistoryDetailResponse resp = new TransHistoryDetailResponse();
		try {
			resp.setTransHistoryDetailList(transHistoryDetailService.getTransHistoryDetailList(req.getTransNums()));
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setReturnHeader(returnHeader);
			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getTransHistoryDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}