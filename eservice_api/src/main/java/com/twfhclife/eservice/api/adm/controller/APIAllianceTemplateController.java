package com.twfhclife.eservice.api.adm.controller;

import com.twfhclife.alliance.service.impl.MedicalTreatmentExternalServiceImpl;
import com.twfhclife.eservice.api.adm.domain.APIAllianceRequestVo;
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

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
public class APIAllianceTemplateController extends BaseController {

	private static final Logger logger = LogManager.getLogger(APIAllianceTemplateController.class);


	@Autowired
	private MedicalTreatmentExternalServiceImpl medicalExternalServiceImpl;

	
	/**
	 * 調用API的模板
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/alliance-template/APIAlliance", produces = { "application/json" })
	public ResponseEntity<?> triggerAPIAllianceTemplate(
			@Valid @RequestBody APIAllianceRequestVo  vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<String> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			String url = vo.getUrl();
			Map<String, String> params = vo.getParams();
			Map<String, String> unParams = vo.getUnParams();
			String resultMsg = medicalExternalServiceImpl.postForEntity(url, params, unParams);
			//String resultMsg = "{\"code\":\"0\",\"msg\":\"success\"}";
			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
				returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "", "", "");
				apiResponseObj.setResult("Send message successfully complete.");
			} else {
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, resultMsg, "", "");
			}
			
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, "", "", "");
			logger.error("Unable to triggerAPIAllianceTemplate: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

}
