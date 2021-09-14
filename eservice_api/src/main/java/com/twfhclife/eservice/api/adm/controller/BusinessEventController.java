package com.twfhclife.eservice.api.adm.controller;

import java.util.Date;

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

import com.twfhclife.eservice.api.adm.domain.EventRecordAddVo;
import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.adm.service.IBusinessEventService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.PageResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class BusinessEventController extends BaseController {

	private static final Logger logger = LogManager.getLogger(BusinessEventController.class);

	@Autowired
	public IBusinessEventService businessEventService;
	
	@ApiRequest
	@PostMapping(value = "/event-record/add", produces = { "application/json" })
	public ResponseEntity<?> addBusinessEvent(@Valid @RequestBody EventRecordAddVo vo) {
		ApiResponseObj<PageResponseObj<ParameterVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		int result = 0;
		try {
			for(BusinessEventVo evo : vo.getBusinessEventList()) {
				int nextBusinessEventId = businessEventService.getNextId();
				evo.setBusinessEventId(nextBusinessEventId);
				evo.setCreateDate(new Date());
				evo.setCreateUser(vo.getUserId());
				result += businessEventService.add(evo);
			}
			
			if (result > 0) {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "新增成功", "", "");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "新增失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to add business event: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
}
