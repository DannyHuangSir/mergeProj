package com.twfhclife.eservice.api.elife.controller;

import java.util.LinkedList;
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

import com.twfhclife.eservice.api.elife.domain.PolicyAcctValueRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyAcctValueResponse;
import com.twfhclife.eservice.api.elife.domain.UserPolicyAcctValueRequest;
import com.twfhclife.eservice.api.elife.domain.UserPolicyAcctValueResponse;
import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;
import com.twfhclife.eservice.policy.service.IPolicyAccountValueService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 以保單號碼查詢保單帳戶價值.
 * 
 * @author all
 */
@RestController
public class PolicyAcctValueController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyAcctValueController.class);
	
	@Autowired
	private ILilipmService lilipmService;

	@Autowired
	private IPolicyAccountValueService policyAccountValueService;
	
	/**
	 * 以保單號碼查詢保單帳戶價值.
	 * 
	 * @param req PolicyAcctValueRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-009", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyAccountValueDao.getPolicyAccountValueList",
					execMethod = "以保單號碼查詢保單帳戶價值",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyValueByPolicyNo", produces = { "application/json" })
	public ResponseEntity<?> getPolicyValueByPolicyNo(@Valid @RequestBody PolicyAcctValueRequest req) {
		ApiResponseObj<PolicyAcctValueResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyAcctValueResponse resp = new PolicyAcctValueResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				List<PolicyAccountValueVo> policyAccountValueList = policyAccountValueService.getPolicyAccountValueList(policyNo);
				resp.setPolicyAccountValueList(policyAccountValueList);
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyValueByPolicyNo: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	/**
	 * 以要保人身分證字號為條件查詢保單帳戶價值.
	 * 
	 * @param req UserPolicyAcctValueRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-010", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.user.dao.LilipmDao.getUserPolicyNos",
					execMethod = "以要保人身分證字號為條件查詢保單帳戶價值",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				)
			}))
	@PostMapping(value = "/getPolicyValueByRocId", produces = { "application/json" })
	public ResponseEntity<?> getPolicyValueByRocId(@Valid @RequestBody UserPolicyAcctValueRequest req) {
		ApiResponseObj<UserPolicyAcctValueResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		UserPolicyAcctValueResponse resp = new UserPolicyAcctValueResponse();
		List<PolicyAccountValueVo> userAccountValueList = new LinkedList<>();
		
		try {
			
			String rocId = req.getPolicyHolderId();
			List<String> policyNos = lilipmService.getUserPolicyNos(rocId);
			if (policyNos != null) {
				for (String policyNo : policyNos) {
					List<PolicyAccountValueVo> policyAccountValueList = policyAccountValueService.getPolicyAccountValueList(policyNo);
					if (policyAccountValueList != null && policyAccountValueList.size() > 0) {
						userAccountValueList.addAll(policyAccountValueList);
					}
				}
				
				resp.setPolicyAccountValueList(userAccountValueList);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyValueByRocId: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}