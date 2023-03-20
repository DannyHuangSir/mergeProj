package com.twfhclife.adm.controller.jd;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.JdPolicyClaimDetailVo;
import com.twfhclife.adm.service.IJdPolicyClaimDetailService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
        return  "backstage/jd/policyClaimDetail1";
    }

    @RequestLog
    @PostMapping("/policyClaimDetail/filter")
    public String policyClaimDetailFilter(JdPolicyClaimDetailVo vo) {
        addAttribute("vo", vo);
        return "backstage/jd/policyClaimDetail2";
    }


    @RequestLog
    @PostMapping("/policyClaimDetail/csv")
    public String policyClaimDetailCSV1(JdPolicyClaimDetailVo vo) {
        PolicyClaimDetailResponse report1 = jdPolicyClaimDetailService.getInsClaimStatisticsReport(vo);
        List<JdPolicyClaimDetailVo> reportList = report1.getPolicyClaimDetailVo();
        addAttribute("vo", vo);
        addAttribute("reportList", reportList);
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

