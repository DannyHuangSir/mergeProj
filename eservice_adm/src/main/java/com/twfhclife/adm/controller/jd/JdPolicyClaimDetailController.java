package com.twfhclife.adm.controller.jd;

import com.google.common.collect.Lists;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IJdPolicyClaimDetailService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
import com.twfhclife.generic.model.UserDetailResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @auther lihao
 */
@Controller
public class JdPolicyClaimDetailController extends BaseController {


    @Autowired
    private IJdPolicyClaimDetailService jdPolicyClaimDetailService;

    private static final Logger logger = LoggerFactory.getLogger(JdPolicyClaimDetailController.class);

    @GetMapping("/policyClaimDetail")
    public String rptInsClaimDetail() {
        addAttribute("isGoBack", false);
        return  "backstage/jd/policyClaimDetail1";
    }

    @RequestLog
    @PostMapping("/returnPolicyClaimDetailStep1")
    public String returnPolicyClaimDetailStep1(JdPolicyClaimReqVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", true);
        return  "backstage/jd/policyClaimDetail1";
    }

    @GetMapping("/userDetail")
    public String userDetail() {
        addAttribute("isGoBack", false);
        return "backstage/jd/userDetail";
    }

    @RequestLog
    @PostMapping("/returnStep1")
    public String returnStep1(JdUserDetailReqVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", true);
        return "backstage/jd/userDetail";
    }

    @RequestLog
    @PostMapping("/userDetail/filter")
    public String userDetailFilter(JdUserDetailVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", false);
        return "backstage/jd/userDetail2";
    }

    @RequestLog
    @PostMapping("/returnStep2")
    public String returnStep2(JdUserDetailReqVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", true);
        return "backstage/jd/userDetail2";
    }

    @RequestLog
    @PostMapping("/policyClaimDetail/filter")
    public String policyClaimDetailFilter(JdPolicyClaimDetailVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", false);
        return "backstage/jd/policyClaimDetail2";
    }

    @RequestLog
    @PostMapping("/returnPolicyClaimDetailStep2")
    public String returnPolicyClaimDetailStep2(JdPolicyClaimReqVo vo) {
        addAttribute("vo", vo);
        addAttribute("isGoBack", true);
        return "backstage/jd/policyClaimDetail2";
    }

    @RequestLog
    @PostMapping("/userDetail/csv")
    public String userDetailCsv(JdUserDetailReqVo vo) {
        UserDetailResponse report1 = jdPolicyClaimDetailService.getUserDetail(vo);
        addAttribute("vo", vo);
        addAttribute("reportList", report1 != null ? report1.getUserDetailVos() : Lists.newArrayList());
        return "backstage/jd/userDetail3";
    }


    @RequestLog
    @PostMapping("/policyClaimDetail/csv")
    public String policyClaimDetailCSV1(JdPolicyClaimReqVo vo) {
        PolicyClaimDetailResponse report1 = jdPolicyClaimDetailService.getInsClaimStatisticsReport(vo);
        addAttribute("vo", vo);
        addAttribute("reportList", report1 != null ? report1.getPolicyClaimDetailVo() : Lists.newArrayList());
        return   "backstage/jd/policyClaimDetail3";
    }

    @PostMapping("/getBpmcurrenttak")
    public ResponseEntity<ResponseObj> getBpmcurrenttak(JdPolicyClaimDetailVo vo){
        try {
            processSuccess(jdPolicyClaimDetailService.getBpmcurrenttak());
        }catch (Exception e){
            logger.error("Unable to getBpmcurrenttak: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


    @PostMapping("/jd/getPolicyTypeNameList")
    public ResponseEntity<ResponseObj> getPolicyTypeNameList(JdPolicyClaimDetailVo vo){
        try {
            PolicyClaimDetailResponse  policyTypeName = jdPolicyClaimDetailService.getPolicyTypeNameList(vo);
            processSuccess(policyTypeName.getPolicyClaimDetailVo());
        }catch (Exception e){
            logger.error("Unable to getPolicyTypeNameList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


}

