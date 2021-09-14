package com.twfhclife.eservice.api.adm.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.IEventRecordService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.MyStringUtil;

/**
 * 紀錄管理.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class EventRecordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(EventRecordController.class);

	@Autowired
	private IEventRecordService eventRecordService;


	@ApiRequest
	@PostMapping(value = "/event-record/searches", produces = { "application/json" })
	public ResponseEntity<?> getEventRecordTable(@RequestBody BusinessEventVo vo) {
		ApiResponseObj<List<BusinessEventVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (MyStringUtil.isNullOrEmpty(vo.getUserId()) && MyStringUtil.isNullOrEmpty(vo.getEventCode())
					&& MyStringUtil.isNullOrEmpty(vo.getEventName())) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "userId、eventCode、eventName欄位至少輸入一項", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseObj);
			}
			
			logger.info("getEventRecordTable. sysId = "+vo.getTargetSystemId() +" userId = "+vo.getUserId() + 
					" eventName = "+vo.getEventName());
			List<BusinessEventVo> results = eventRecordService.getEventRecordTable(vo);
			apiResponseObj.setResult(results);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getEventRecordTable: {}", ExceptionUtils.getStackTrace(e));
			//Map<String, Object> error = Collections.singletonMap("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}		
		
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@RequestLog
	@PostMapping("/eventRecord/getBusinessEventDetail")
	public ResponseEntity<ResponseObj> getBusinessEventDetail(@RequestBody String businessEventId) {
		BusinessEventVo result = eventRecordService.getBusinessEventDetail(businessEventId);
		this.setResponseObj(ResponseObj.SUCCESS, "", result);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@PostMapping("/eventRecord/getSystemEventDetail")
	public ResponseEntity<ResponseObj> getSystemEventDetail(@RequestBody String businessEventId) {
		List<SystemEventVo> results = eventRecordService.getSystemEventDetail(businessEventId);
		this.setResponseObj(ResponseObj.SUCCESS, "", results);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
}
