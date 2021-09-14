package com.twfhclife.eservice.policy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.policy.model.AgentVo;
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
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PolicyDataClient;
import com.twfhclife.generic.api_model.PolicyDataResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 保單資料.
 * 
 * @author alan
 */
@Controller
public class PolicyDataController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PolicyDataController.class);
	
	public static final String ADMIN_QUERY_ROCID = "ADMIN_QUERY_ROCID";
	
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
	private PolicyDataClient policyDataClient;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;
	
	@Autowired
	private ILoginService loginService;
	
	/**
	 * 保單資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing1")
	public String listing1(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<CoverageVo> coverageVoList = null; // 保項清單
			LilipmVo lilipmVo = null;               // 要保人
			LilipiVo lilipiVo = null;               // 主被險人
			PolicyPayerVo policyPayerVo = null;     // 付款人
			AgentVo agentVo = null;                 // 招攬人
			List<BeneficiaryVo> beneficiaryList = null;	// 受益人
			UsersVo userDetail = (UsersVo)getSession(UserDataInfo.USER_DETAIL);
			List<LilipiVo> lilipiVoList = new ArrayList<LilipiVo>();
			// Call api 取得資料
			PolicyDataResponse policyDataResponse = policyDataClient.getPolicyDetail(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (policyDataResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyDetail]", userId);
				
				coverageVoList = policyDataResponse.getCoverageList();
				lilipmVo = policyDataResponse.getLilipmVo();
				lilipiVo = policyDataResponse.getLilipiVo();
				policyPayerVo = policyDataResponse.getPolicyPayerVo();
				agentVo = policyDataResponse.getAgentVo();
				beneficiaryList = policyDataResponse.getBeneficiaryList();
			} else {
				logger.info("Call internal service to get user[{}] policyDetail data", userId);
				
				CoverageVo coverageVo = new CoverageVo();
				coverageVo.setPolicyNo(policyNo);
				
				coverageVoList = coverageService.getCoverageList(coverageVo);
				lilipmVo = lilipmService.findByPolicyNo(policyNo);
				lilipiVo = lilipiService.findByPolicyNo(policyNo);
				policyPayerVo = policyPayerService.getPolicyPayer(policyNo);
				
				PolicyVo policyVo = policyListService.findById(policyNo);
				if (policyVo != null) {
					agentVo = agentService.getAgent(policyVo.getAgentCode());
				}
				beneficiaryList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
			}
			
			if (coverageVoList != null && coverageVoList.size() > 0) {
				for (CoverageVo c : coverageVoList) {
					if (c.getExpireDate() != null && DateUtil.formatDate(c.getExpireDate(), "yyyy").equals("2910")) {
						c.setIsWholeLife("Y");
					}
					c.setInsuredNameBase64(unicodeService.convertString2Unicode(c.getInsuredName()));
				}
			}
			
			//CR05 我的保單，增加受益人資料
			if (beneficiaryList != null && beneficiaryList.size() > 0) {
				Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
				if (sysParamMap.containsKey("BENEFICIARY_TYPE")) {
					Map<String, ParameterVo> paraMap = sysParamMap.get("BENEFICIARY_TYPE");
					for (BeneficiaryVo vo : beneficiaryList) {
						if (paraMap != null && paraMap.containsKey(String.valueOf(vo.getBeneficiaryType()))) {
							ParameterVo param = paraMap.get(String.valueOf(vo.getBeneficiaryType()));
							vo.setBeneficiaryTypeStr(param.getParameterValue());
						}
						vo.setBeneficiaryNameBase64(unicodeService.convertString2Unicode(vo.getBeneficiaryName()));
					}
				}
			}
			
			addAttribute("coverageList", coverageVoList);
			addAttribute("customerVo", lilipmVo);
			addAttribute("insuredCoverage", lilipiVo);
			addAttribute("payerVo", policyPayerVo);
			addAttribute("agentVo", agentVo);
			addAttribute("beneficiaryList", beneficiaryList);
			
			//modify for 保單理賠-admin/agent登入-start
			String adminQueryRocId = null;
			
			if(userDetail!=null) {
				boolean isPartner = loginService.isPartnerRole(userDetail.getUserType());
				if(isPartner) {//因為來自API的物件不會回傳@JsonIgnore attribute
					logger.info("***Login type is admin/agent,re-get lilpmvo/lilipivo from service.***");
					lilipmVo = lilipmService.findByPolicyNo(policyNo);
					lilipiVo = lilipiService.findByPolicyNo(policyNo);
				}
			}
			
			//要保人ROCID為空，則取被保人ROCID
			if(lilipmVo!=null && StringUtils.isNotBlank(lilipmVo.getNoHiddenLipmId())) {
				adminQueryRocId = lilipmVo.getNoHiddenLipmId().trim();
				logger.info("lilipmVo.getNoHiddenLipmId()="+adminQueryRocId);
			}else if(lilipiVo!=null && StringUtils.isNotBlank(lilipiVo.getNoHiddenLipiId())){
				adminQueryRocId = lilipiVo.getNoHiddenLipiId().trim();
				logger.info("lilipiVo.getNoHiddenLipiId()="+adminQueryRocId);
			}else {
				logger.info("lilipmVo/lilipiVo get RocId all null.");
			}
			logger.info("Last ADMIN_QUERY_ROCID="+adminQueryRocId);
			addSession("ADMIN_QUERY_ROCID",adminQueryRocId);
			//modify for 保單理賠-admin/agent登入-end
			
		} catch (Exception e) {
			logger.error("Unable to get data from listing1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		
		return "frontstage/listing1";
	}
}