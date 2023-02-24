package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.PolicyBaseVo;
import com.twfhclife.eservice.web.model.PolicySafeGuardVo;
import com.twfhclife.eservice.web.model.PolicyVo;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Policy;

@Controller
public class JdzqController extends BaseController {

    private static final Logger logger = LogManager.getLogger(JdzqController.class);

    @GetMapping("/policyQuery")
    public String policyQuery() {
        return "frontstage/jdzq/policyQuery/policy-query";
    }

    @Autowired
    private IPolicyService policyService;

    @PostMapping(value = { "/getPolicyList" })
    @ResponseBody
    public ResponseObj getPolicyList(@RequestBody PolicyVo vo) {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        responseObj.setResultData(policyService.getPolicyList(getLoginUser(), vo));
        return responseObj;
    }

    @GetMapping("/protectQuery")
    public String protectQuery() {
        return "frontstage/jdzq/protectQuery/protect-query";
    }

    @GetMapping("/caseQuery")
    public String caseQuery() {
        return "frontstage/jdzq/caseQuery/case-query";
    }

    @RequestMapping("/listing1")
    public String listing1(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyBaseVo vo = policyService.getPolicyBase(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing1: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing1";
    }

    @RequestMapping("/listing11")
    public String listing11(@RequestParam("policyNo") String policyNo) {
        try {
            PolicySafeGuardVo vo = policyService.getPolicyGuard(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing11: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing11";
    }

    @RequestMapping("/listing3")
    public String listing3(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing3: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing3";
    }
}