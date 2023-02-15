package com.twfhclife.eservice.web.controller;

import com.google.common.collect.Lists;
import com.sun.deploy.association.utility.AppConstants;
import com.twfhclife.eservice.api_client.PolicyDataClient;
import com.twfhclife.eservice.api_model.PolicyDataResponse;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.service.ICoverageService;
import com.twfhclife.eservice.service.ILilipiService;
import com.twfhclife.eservice.service.ILilipmService;
import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.web.model.AgentVo;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.*;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class JdzqController extends BaseController {

    private static final Logger logger = LogManager.getLogger(JdzqController.class);

    @Autowired
    private IPolicyService policyService;

    @GetMapping("/policyQuery")
    public String policyInquiry() {
        addAttribute("policyList", policyService.getPolicyList());
        return "frontstage/jdzq/policyQuery/policy-query";
    }

    @GetMapping("/protectQuery")
    public String protectQuery() {
        return "frontstage/jdzq/protectQuery/protect-query";
    }

    @GetMapping("/caseQuery")
    public String caseQuery() {
        return "frontstage/jdzq/caseQuery/case-query";
    }

    @Autowired
    private PolicyDataClient policyDataClient;

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

    @Autowired
    private ILoginService loginService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    public IUnicodeService unicodeService;

    @RequestMapping("/listing1")
    public String listing1(@RequestParam("policyNo") String policyNo) {
        try {
            List<CoverageVo> coverageVoList = null; // 保項清單
            LilipmVo lilipmVo = null;               // 要保人
            LilipiVo lilipiVo = null;               // 主被險人
            PolicyPayerVo policyPayerVo = null;     // 付款人
            AgentVo agentVo = null;                 // 招攬人
            List<BeneficiaryVo> beneficiaryList = null;    // 受益人
            UsersVo userDetail = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
            // Call api 取得資料
            PolicyDataResponse policyDataResponse = policyDataClient.getPolicyDetail(policyNo);
            // 若無資料，嘗試由內部服務取得資料
            if (policyDataResponse != null) {
                coverageVoList = policyDataResponse.getCoverageList();
                lilipmVo = policyDataResponse.getLilipmVo();
                lilipiVo = policyDataResponse.getLilipiVo();
                policyPayerVo = policyDataResponse.getPolicyPayerVo();
                agentVo = policyDataResponse.getAgentVo();
                beneficiaryList = policyDataResponse.getBeneficiaryList();
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


			lilipmVo = lilipmService.findByPolicyNo(policyNo);
			lilipiVo = lilipiService.findByPolicyNo(policyNo);

            //要保人ROCID為空，則取被保人ROCID
            if (lilipmVo != null && StringUtils.isNotBlank(lilipmVo.getNoHiddenLipmId())) {
                adminQueryRocId = lilipmVo.getNoHiddenLipmId().trim();
                logger.info("lilipmVo.getNoHiddenLipmId()=" + adminQueryRocId);
            } else if (lilipiVo != null && StringUtils.isNotBlank(lilipiVo.getNoHiddenLipiId())) {
                adminQueryRocId = lilipiVo.getNoHiddenLipiId().trim();
                logger.info("lilipiVo.getNoHiddenLipiId()=" + adminQueryRocId);
            } else {
                logger.info("lilipmVo/lilipiVo get RocId all null.");
            }
            logger.info("Last ADMIN_QUERY_ROCID=" + adminQueryRocId);
            addSession("ADMIN_QUERY_ROCID", adminQueryRocId);
            //modify for 保單理賠-admin/agent登入-end

        } catch (Exception e) {
            logger.error("Unable to get data from listing1: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }

        return "frontstage/listing1";
    }

    @PostMapping("/listing2")
    public String listing2(@RequestParam("policyNo") String policyNo) {
        try {

            List<ParameterVo> parameterVos = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "LISTING_MAP_TEMPLATE");
            if (!CollectionUtils.isEmpty(parameterVos)) {
                for (ParameterVo parameterVo : parameterVos) {
                    if (parameterVo.getParameterCode().endsWith(policyNo.substring(0, 2))) {
                        logger.info("listing2 -> : {}", parameterVo.getParameterValue());
                        return parameterVo.getParameterValue();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unable to getRiskLevelName from listing2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        logger.info("listing2 -> : {}", "frontstage/listing2");
        return "frontstage/listing2";
    }
}