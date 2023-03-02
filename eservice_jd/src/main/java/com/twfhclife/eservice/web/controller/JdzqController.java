package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.api_model.PolicyFundTransactionResponse;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.util.DateUtil;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
            PolicyPaymentRecordVo vo = policyService.getPolicyPaymentRecord(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing3: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing3";
    }

    @RequestMapping("/listing5")
    public String listing5(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyPremiumVo vo = policyService.getPolicyPremium(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing5: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing5";
    }

    @RequestMapping("/listing13")
    public String listing13(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyExpireOfPaymentVo vo = policyService.getPolicyExpireOfPayment(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing13: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing13";
    }

    @RequestMapping("/listing5_3")
    public String listing5_3(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyChangeInfoVo vo = policyService.getChangeInfo(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing5_3: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing5-3";
    }

    @RequestMapping("/listing2")
    public String listing2(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyChangeInfoVo vo = policyService.getChangeInfo(policyNo);
            addAttribute("vo", vo);
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing2";
    }

    @RequestMapping("/listing6_2")
    public String listing6_2(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyIncomeDistributionVo vo = policyService.getIncomeDistribution(policyNo);
            addAttribute("vo", vo.getPolicy());
            addAttribute("info", vo.getIncomeDistributions());
            addAttribute("policyNo", policyNo);
            BigDecimal sumAmount = BigDecimal.valueOf(0);
            for (IncomeDistributionVo incomeDistribution : vo.getIncomeDistributions()) {
                sumAmount = sumAmount.add(incomeDistribution.getExpeNtd());
            }
            addAttribute("sumAmount", sumAmount);
        } catch (Exception e) {
            logger.error("Unable to get data from listing6_2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing6-2";
    }

    @RequestMapping("/listing10")
    public String listing10(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyIncomeDistributionVo vo = policyService.getIncomeDistribution(policyNo);
            addAttribute("vo", vo.getPolicy());
            addAttribute("policyNo", policyNo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing10: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing10";
    }

    @PostMapping("/getTxLogList")
    public ResponseEntity<ResponseObj> getTxLogList(@RequestParam("policyNo") String policyNo,
                                                    @RequestParam(value = "trCode", required = false) String trCode,
                                                    @RequestParam(value = "startDate", required = false) String startDate,
                                                    @RequestParam(value = "endDate", required = false) String endDate,
                                                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
        try {
            String errorMessage = "";
            int defaultPageSize = 4;
            trCode = (StringUtils.isEmpty(trCode) ? null : trCode);

            if (startDate != null && endDate != null) {
                if (startDate.compareTo(endDate) > 0) {
                    errorMessage = "結束日期不能小於開始日期";
                }
            }

            if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
                Date d = new Date();
                d.setYear(d.getYear() - 2);
                startDate = DateUtil.formatDateTime(d, "yyyy/MM/dd");
                endDate = DateUtil.formatDateTime(new Date(), "yyyy/MM/dd");
            }

            if (StringUtils.isEmpty(errorMessage)) {
                List<FundTransactionVo> fundTransactionList = null;
                PolicyFundTransactionResponse policyFundTransactionResponse = policyService
                        .getPolicyFundTransaction(getUserId(), policyNo, trCode, startDate, endDate, pageNum, defaultPageSize);
                logger.info("Get user[{}] data from eservice_api[getPolicyFundTransactionPageList]");
                fundTransactionList = policyFundTransactionResponse.getFundTransactionList();
                processSuccess(fundTransactionList);
            } else {
                processError(errorMessage);
            }
        } catch (Exception e) {
            logger.error("Unable to getTxLogList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }
}