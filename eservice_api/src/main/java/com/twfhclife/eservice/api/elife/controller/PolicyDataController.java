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

import com.twfhclife.eservice.api.elife.domain.PolicyDataRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyDataResponse;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.policy.model.PolicyVo;
import com.twfhclife.eservice.policy.service.IAgentService;
import com.twfhclife.eservice.policy.service.ICoverageService;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPolicyPayerService;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 查詢保單明細資料.
 * 
 * @author all
 */
@RestController
public class PolicyDataController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyDataController.class);
	
	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private ICoverageService coverageService;
	
	@Autowired
	private IAgentService agentService;
	
	@Autowired
	private IPolicyPayerService policyPayerService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;

	/**
	 * 查詢保單明細資料.
	 * 
	 * @param req PolicyDataRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-004", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.CoverageDao.getCoverageList",
					execMethod = "查詢保障項目",
					sqlVoType = "com.twfhclife.eservice.policy.model.CoverageVo",
					sqlVoKey = "coverageVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.user.dao.LilipmDao.findByPolicyNo",
					execMethod = "查詢要保人",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.user.dao.LilipiDao.findByPolicyNo",
					execMethod = "查詢被保人",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyPayerDao.getPolicyPayer",
					execMethod = "查詢付款人",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.onlineChange.dao.BeneficiaryDao.getBeneficiaryByPolicyNo",
					execMethod = "查詢受益人",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
					}
				)
			}))
	@PostMapping(value = "/getPolicyDetail", produces = { "application/json" })
	public ResponseEntity<?> getPolicyDetail(@Valid @RequestBody PolicyDataRequest req) {
		ApiResponseObj<PolicyDataResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PolicyDataResponse resp = new PolicyDataResponse();
		
		try {
			String policyNo = req.getPolicyNo();
			if (!StringUtils.isEmpty(policyNo)) {
				CoverageVo coverageVo = new CoverageVo();
				coverageVo.setPolicyNo(policyNo);
				
				// 保項清單
				List<CoverageVo> covs = coverageService.getCoverageList(coverageVo);
				resp.setCoverageList(covs);
				
				// 要保人
				LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
				resp.setLilipmVo(lilipmVo);
			
				// 主被險人
				LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
				resp.setLilipiVo(lilipiVo);
				
				// 付款人
				PolicyPayerVo policyPayerVo = policyPayerService.getPolicyPayer(policyNo);
				resp.setPolicyPayerVo(policyPayerVo);
				
				// 招攬人
				PolicyVo policyVo = policyListService.findById(policyNo);
				if (policyVo != null) {
					resp.setAgentVo(agentService.getAgent(policyVo.getAgentCode()));
				}
				
				// 受益人
				List<BeneficiaryVo> beneficiaryList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
				if (beneficiaryList != null) {
					resp.setBeneficiaryList(beneficiaryList);
				}
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyDetail: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}