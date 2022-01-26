package com.twfhclife.eservice.policy.controller;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.PolicyReversalVo;
import com.twfhclife.eservice.policy.service.IPolicyReversalService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.controller.BaseController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PolicyReversalController extends BaseController {


    private static final Logger logger = LogManager.getLogger(PolicyReversalController.class);

    @Autowired
    private IPolicyReversalService policyReversalService;

    /**
     * 撥回資料頁面
     *
     * @return
     */
    @RequestLog
    @PostMapping("/listing13")
    public String listing13(@RequestParam("policyNo") String policyNo) {
        return "frontstage/listing13";
    }

    /**
     * 撥回資料查詢
     * @param policyNo 保單號碼
     * @return
     */
    @RequestLog
    @PostMapping("/getPolicyReversalList")
    public ResponseEntity<ResponseObj> getPolicyReversalList(@RequestParam("policyNo") String policyNo) {

        try {
            String userId = getUserId();
            logger.info("Call internal service to get user[{}], policyNo[{}] getPolicyReversalList data", userId);
            List<PolicyReversalVo> policyReversalVoList = policyReversalService.findByPolicyNo(policyNo);
            for (PolicyReversalVo policyReversalVo : policyReversalVoList) {
                policyReversalVo.fixData();
            }
            processSuccess(policyReversalVoList);
        } catch (Exception e) {
            logger.error("Unable to getPolicyReversalList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


}
