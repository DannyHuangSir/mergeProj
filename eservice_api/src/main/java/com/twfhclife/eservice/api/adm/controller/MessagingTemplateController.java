package com.twfhclife.eservice.api.adm.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.adm.domain.MessagingTemplateReqVo;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.PageResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyStringUtil;

@RestController
public class MessagingTemplateController extends BaseController {

	private static final Logger logger = LogManager.getLogger(MessagingTemplateController.class);
	
	@Autowired
	IMessagingTemplateService messagingTemplateService;
	
	/**
	 * 通信管理API.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/message-template/trigger", produces = { "application/json" })
	public ResponseEntity<?> triggerMessagingTemplate(@Valid @RequestBody MessageTriggerRequestVo vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<String> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/message-template/trigger:" + ApConstants.INVALID_SECRET);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
					
			String resultMsg = messagingTemplateService.triggerMessageTemplate(vo);

			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setResult("Send message successfully complete.");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, resultMsg, "", "");
			}
			
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "", "", "");
			logger.error("Unable to triggerMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	@ApiRequest
	@PostMapping(value = "/message-template/searches", produces = { "application/json" })
	public ResponseEntity<?> getMessagingTemplate(@Valid @RequestBody MessagingTemplateReqVo vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<PageResponseObj<Map<String, Object>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PageResponseObj<Map<String, Object>> pageResp = new PageResponseObj<>();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/message-template/searches:" + ApConstants.INVALID_SECRET);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
					
//			List<Map<String, Object>> map = messagingTemplateService.getMessagingTemplatePageList(vo);
//			// Note: MessagingTemplateVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
//			// 設定jqGrid 資料查詢集合
//			pageResp.setRows(map);
//			// 設定jqGrid 資料查詢總筆數
//			pageResp.setRecords(messagingTemplateService.getMessagingTemplatePageTotal(vo));
//			// 設定jqGrid 目前頁數
//			pageResp.setPage(messagingTemplateVo.getPage());
//			// 設定jqGrid 總頁數
//			pageResp.setTotal(
//					(pageResp.getRecords() + messagingTemplateVo.getRows() - 1) / messagingTemplateVo.getRows());
//			pageResp.setResult(PageResponseObj.SUCCESS);
//
//			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
//				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
//				apiResponseObj.setResult("Send message successfully complete.");
//			} else {
//				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, resultMsg, "", "");
//			}
			
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "", "", "");
			logger.error("Unable to triggerMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}
