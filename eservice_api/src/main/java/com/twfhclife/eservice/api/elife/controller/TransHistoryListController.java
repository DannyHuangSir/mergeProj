package com.twfhclife.eservice.api.elife.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.elife.domain.TransHistoryListRequest;
import com.twfhclife.eservice.api.elife.domain.TransHistoryListResponse;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ValidateUtil;

/**
 * 查詢線上申請紀錄.
 * 
 * @author all
 */
@RestController
public class TransHistoryListController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TransHistoryListController.class);
	
	@Autowired
	private IOnlineChangeService onlineChangeService;

	/**
	 * 查詢線上申請紀錄.
	 * 
	 * @param req TransHistoryPageRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-016", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao.getTransByUserId",
					execMethod = "查詢線上申請紀錄",
					sqlParams = { 
						@SqlParam(requestParamkey = "userId", sqlParamkey = "userId"),
						@SqlParam(requestParamkey = "transStatus", sqlParamkey = "status"),
						@SqlParam(requestParamkey = "transType", sqlParamkey = "transType"),
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
						@SqlParam(requestParamkey = "startDate", sqlParamkey = "startDate"),
						@SqlParam(requestParamkey = "endDate", sqlParamkey = "endDate"),
						@SqlParam(requestParamkey = "pageNum", sqlParamkey = "pageNum"),
						@SqlParam(requestParamkey = "pageSize", sqlParamkey = "pageSize")
					}
				)
			}))
	@PostMapping(value = "/getTransHistoryList", produces = { "application/json" })
	public ResponseEntity<?> getTransHistoryList(@Valid @RequestBody TransHistoryListRequest req) {
		String errorMessage = "";
		if(!ValidateUtil.TransHistoryListRequestIsValid(req)) {
			errorMessage = "參數異常，請洽客服人員。";
		}
		
		ApiResponseObj<TransHistoryListResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		TransHistoryListResponse resp = new TransHistoryListResponse();
		try {
			String userId = req.getUserId();
			String transStatus = StringUtils.trimToNull(req.getTransStatus());
			String transType = StringUtils.trimToNull(req.getTransType());
			String policyNo = StringUtils.trimToNull(req.getPolicyNo());
			String startDate = StringUtils.trimToNull(req.getStartDate());
			String endDate = StringUtils.trimToNull(req.getEndDate());
			Integer pageSize = req.getPageSize();
			Integer pageNum = req.getPageNum();
			
			if (startDate != null && endDate != null) {
				if (startDate.compareTo(endDate) > 0) {
					errorMessage = "結束日期不能小於開始日期";
				}
			}
			
			if (StringUtils.isEmpty(errorMessage)) {
				List<TransVo> transHistoryList = onlineChangeService.getTransByUserId(userId, transStatus, transType,
						policyNo, startDate, endDate, pageNum, pageSize);
				resp.setTransHistoryList(transHistoryList);
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
				for(TransVo vo : transHistoryList) {
					logger.info("transNum:{}, endorsementStatus:{}", vo.getTransNum(), vo.getEndorsementStatus());
					logger.info("----------------------------getTransTypeStr:{}-----------------------", vo.getTransTypeStr());
					logger.info("----------------------------getFromCompanyId:{}-----------------------", vo.getFromCompanyId());
				}
			} else {
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, errorMessage, "", "");
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getTransHistoryList: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}