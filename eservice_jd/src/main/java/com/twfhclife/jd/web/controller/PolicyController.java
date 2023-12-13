package com.twfhclife.jd.web.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.jd.api_model.*;
import com.twfhclife.jd.controller.BaseController;
import com.twfhclife.jd.keycloak.model.KeycloakUser;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.util.DateUtil;
import com.twfhclife.jd.web.dao.UsersDao;
import com.twfhclife.jd.web.domain.PortfolioResponseObj;
import com.twfhclife.jd.web.domain.ResponseObj;
import com.twfhclife.jd.web.model.*;
import com.twfhclife.jd.web.service.IOptionService;
import com.twfhclife.jd.web.service.IPolicyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class PolicyController extends BaseController {

    private static final Logger logger = LogManager.getLogger(PolicyController.class);

    @Autowired
    private UsersDao usersDao;
    @GetMapping("/policyQuery")
    public String policyQuery() {
        addAttribute("queryPolicy", new PolicyVo());
        addAttribute("autoQuery", false);
        addAttribute("policyTypeList", optionService.getPolicyTypeList());
        KeycloakUser user = getLoginUser();
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員 5 平台管理人員
        int role = usersDao.checkUserRole(user.getId());
        addAttribute("role", role);

        if(role == 3 || role == 4 || role == 5 ) {
            List<DepartmentVo> deptParentList = usersDao.getDeptParentList(user.getId(), role);
            addAttribute("deptParentList", deptParentList);
        }

        return "frontstage/jdzq/policyQuery/policy-query";
    }

//    @PostMapping(value = { "/clearPolicySearch" })
//    @ResponseBody
//    public void clearPolicySearch() {
//        removeFromSession("queryPolicy");
//    }

    @GetMapping("/returnPolicy")
    public String returnPolicy() {
        PolicyVo vo = (PolicyVo) getSession("queryPolicy");
        addAttribute("queryPolicy", vo == null ? new PolicyVo() : vo);
        addAttribute("autoQuery", vo == null ? false : true);
        addAttribute("policyTypeList", optionService.getPolicyTypeList());
        KeycloakUser user = getLoginUser();
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員 5 平台管理人員
        int role = usersDao.checkUserRole(user.getId());
        addAttribute("role", role);

        if(role == 3 || role == 4 || role == 5 ) {
            List<DepartmentVo> deptParentList = usersDao.getDeptParentList(user.getId(), role);
            addAttribute("deptParentList", deptParentList);
        }

        return "frontstage/jdzq/policyQuery/policy-query";
    }

    @Autowired
    private IPolicyService policyService;

    @PostMapping(value = {"/getPolicyList"})
    @ResponseBody
    public ResponseObj getPolicyList(@RequestBody PolicyVo vo) {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        responseObj.setResultData(policyService.getPolicyList(getLoginUser(), vo));
        addSession("queryPolicy", vo);
        return responseObj;
    }

    @RequestMapping("/listing1")
    public String listing1(@RequestParam("policyNo") String policyNo) {
        try {
            PolicyBaseVo vo = policyService.getPolicyBase(getUserId(), policyNo);
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
            addAttribute("policyNo", policyNo);
            PolicySafeGuardDataResponse vo = policyService.getPolicyGuard(getUserId(), policyNo);
            addAttribute("vo", vo.getPolicyBase());
            addAttribute("safeGuards", vo.getSafeGuard());
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
            PolicyPaymentRecordDataResponse vo = policyService.getPolicyPaymentRecord(getUserId(), policyNo);
            addAttribute("vo", vo.getPolicyBaseVo());
            addAttribute("paymentRecords", vo.getPaymentRecords());
        } catch (Exception e) {
            logger.error("Unable to get data from listing3: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing3";
    }

    @RequestMapping("/listing5")
    public String listing5(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyPremiumDataResponse vo = policyService.getPolicyPremium(getUserId(), policyNo);
            addAttribute("vo", vo.getPolicyBase());
            addAttribute("premiums", vo.getPremiums());
        } catch (Exception e) {
            logger.error("Unable to get data from listing5: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing5";
    }

    @RequestMapping("/listing13")
    public String listing13(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyExpireOfPaymentDataResponse vo = policyService.getPolicyExpireOfPayment(getUserId(), policyNo);
            logger.info("getPolicyExpireOfPayment: result: {}", new Gson().toJson(vo));
            if (vo != null && CollectionUtils.isNotEmpty(vo.getPayments())) {
                List<Map<String, String>> bankList = optionService.getBankList();
                for (ExpireOfPaymentVo vo1 : vo.getPayments()) {
                    for (Map<String, String> map : bankList) {
                        if (StringUtils.equals(map.get("KEY"), vo1.getBankCode())) {
                            vo1.setBankCode(map.get("VALUE"));
                            break;
                        }
                    }
                }
            }
            logger.info("getPolicyExpireOfPayment: vo.getPolicyBase: {}", new Gson().toJson(vo.getPolicyBase()));
            addAttribute("vo", vo == null ? new PolicyBaseVo() : vo.getPolicyBase());
            addAttribute("payments", vo == null ? Lists.newArrayList() : vo.getPayments());
        } catch (Exception e) {
            logger.error("Unable to get data from listing13: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing13";
    }

    @Autowired
    private IOptionService optionService;
    @RequestMapping("/listing5_3")
    public String listing5_3(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyChangeInfoDataResponse vo = policyService.getChangeInfo(getUserId(), policyNo);
            addAttribute("vo", vo.getPolicyBase());
            addAttribute("changeInfos", vo.getChangeInfos());
        } catch (Exception e) {
            logger.error("Unable to get data from listing5_3: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing5-3";
    }

    @RequestMapping("/listing2")
    public String listing2(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyBaseVo vo = policyService.getPolicyBase(getUserId(), policyNo);
            addAttribute("vo", vo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing2";
    }

    @RequestMapping("/listing6_2")
    public String listing6_2(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyIncomeDistributionDataResponse vo = policyService.getIncomeDistribution(getUserId(), policyNo);
            addAttribute("vo", vo.getPolicyBase());
            addAttribute("info", vo.getIncomeDistributions());
            BigDecimal sumAmount = BigDecimal.valueOf(0);
            BigDecimal sumUnits = BigDecimal.valueOf(0);
            for (IncomeDistributionVo incomeDistribution : vo.getIncomeDistributions()) {
                sumAmount = sumAmount.add(incomeDistribution.getExpeNtd());
                if (!StringUtils.equals(incomeDistribution.getTrCode(), "01")) {
                    sumUnits = sumUnits.add(incomeDistribution.getUnits());
                }
            }
            addAttribute("sumAmount", sumAmount);
            addAttribute("sumUnits", sumUnits);
        } catch (Exception e) {
            logger.error("Unable to get data from listing6_2: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing6-2";
    }

    @RequestMapping("/listing10")
    public String listing10(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyBaseVo vo = policyService.getPolicyBase(getUserId(), policyNo);
            addAttribute("vo", vo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing10: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing10";
    }

    @PostMapping("/getTxLogList")
    public ResponseEntity<ResponseObj> getTxLogList(@RequestParam("policyNo") String policyNo,
                                                    @RequestParam(value = "trCode", required = false) String trCode,
                                                    @RequestParam(value = "currency", required = false) String currency,
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

            if (StringUtils.isEmpty(errorMessage)) {
                List<JdFundTransactionVo> fundTransactionList = null;
                JdPolicyFundTransactionResponse policyFundTransactionResponse = policyService
                        .getPolicyFundTransaction(getUserId(), policyNo, trCode, currency, pageNum, defaultPageSize);
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

    @RequestMapping("/listing7")
    public String listing7(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyBaseVo vo = policyService.getPolicyBase(getUserId(), policyNo);
            addAttribute("vo", vo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing7: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing7";
    }

    @PostMapping("/getPremTransList")
    public ResponseEntity<ResponseObj> getPremTransList(@RequestParam("policyNo") String policyNo,
                                                        @RequestParam(value = "startDate", required = false) String startDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate,
                                                        @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
        try {
            String errorMessage = "";
            int defaultPageSize = 4;
            startDate = (StringUtils.isEmpty(startDate) ? null : startDate);
            endDate = (StringUtils.isEmpty(endDate) ? null : endDate);

            if (startDate != null && endDate != null) {
                if (startDate.compareTo(endDate) > 0) {
                    errorMessage = "結束日期不能小於開始日期";
                }
            }
            if (StringUtils.isEmpty(errorMessage)) {
                String userId = getUserId();
                List<FundPrdtVo> premiumTransactionList = null;
                PolicyPremiumCostResponse policyPremiumTransactionResponse = policyService
                        .getPolicyPremiumTransaction(userId, policyNo, startDate, endDate, pageNum, defaultPageSize);
                logger.info("Get user[{}] data from eservice_api[getPolicyPremiumTransactionPageList]", userId);
                premiumTransactionList = policyPremiumTransactionResponse.getPremiumTransactionList();
                processSuccess(premiumTransactionList);
            } else {
                processError(errorMessage);
            }
        } catch (Exception e) {
            logger.error("Unable to getPremTransList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestMapping("/listing16")
    public String listing16(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyInvtFundVo vo = policyService.getPolicyInvtFund(getUserId(), policyNo);
            PolicyBaseVo policyBaseVo =  vo.getPolicy();
            addAttribute("vo", policyBaseVo);
            addAttribute("portfolioList", policyService.getPolicyRateOfReturn(getUserId(), policyNo, policyBaseVo != null ? policyBaseVo.getCurrency() : null).getPortfolioList());
            List<String> rocYearMenu = DateUtil.getYearOpitonByEffectDate("1911/01/01");
            addAttribute("rocYearMenu", rocYearMenu);
        } catch (Exception e) {
            logger.error("Unable to get data from listing16: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing16";
    }

    @PostMapping("/getExchRateChartList")
    public ResponseEntity<ResponseObj> getExchRateChartList(@RequestBody ExchangeRateVo exchangeRateVo) {
        try {
            processSuccess(policyService.getExchangeRate(getUserId(), exchangeRateVo));
        } catch (Exception e) {
            logger.error("Unable to getExchRateChartList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestMapping("/listing17")
    public String listing17(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyCancellationMoneyDataResponse resp = policyService.getPolicyCancellationMoney(getUserId(), policyNo);
            addAttribute("vo", resp.getPolicyVo());
            addAttribute("cancelMoneys", resp.getCancellationMoneyVos());
            addAttribute("amountVo", resp.getPolicyAmountVo() != null ? resp.getPolicyAmountVo() : new PolicyAmountVo());
            addAttribute("isInvest", resp.getInvest());
        } catch (Exception e) {
            logger.error("Unable to get data from listing17: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing17";
    }

    @PostMapping("/getPortfolioList")
    public ResponseEntity<PortfolioResponseObj> getPortfolioList(@RequestParam("policyNo") String policyNo, @RequestParam("currency") String currency) {
        PortfolioResponseObj responseObj = new PortfolioResponseObj();
        try {
            String userId = getUserId();
            List<PortfolioVo> portfolioList = null;
            // Call api 取得資料
            PortfolioResponse portfolioResponse = policyService.getPolicyRateOfReturn(userId, policyNo, currency);
            if (portfolioResponse != null) {
                logger.info("Get user[{}] data from eservice_api[getPolicyloanByPolicyNo]", userId);
                portfolioList = portfolioResponse.getPortfolioList();
            }
            responseObj.setEndDate(portfolioResponse != null ? portfolioResponse.getEndDate() : "");
            responseObj.setResultData(portfolioList);
            responseObj.setResult(ResponseObj.SUCCESS);
        } catch (Exception e) {
            logger.error("Unable to getPortfolioList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }

    @RequestMapping("/listing18")
    public String listing18(@RequestParam("policyNo") String policyNo) {
        try {
            addAttribute("policyNo", policyNo);
            PolicyBaseVo vo = policyService.getPolicyBase(getUserId(), policyNo);
            addAttribute("vo", vo);
        } catch (Exception e) {
            logger.error("Unable to get data from listing18: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/listing18";
    }

    @ResponseBody
    @PostMapping("/updateNotifyConfig")
    public ResponseEntity<ResponseObj> updateNotifyConfig(
            @RequestBody List<NotifyConfigVo> confs) {
        try {
            if (!CollectionUtils.isEmpty(confs)) {
                confs.forEach(c -> c.setUserId(getLoginUser().getId()));
                policyService.updateNotifyConfig(confs);
                this.setResponseObj(ResponseObj.SUCCESS, "", null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
    }

    @PostMapping("/getNotifyPortfolioList")
    public ResponseEntity<ResponseObj> getNotifyPortfolioList(@RequestParam("policyNo") String policyNo, @RequestParam("currency") String currency) {
        try {
            String userId = getUserId();
            List<PortfolioVo> result = Lists.newArrayList();

            // Call api 取得資料
            PortfolioResponse portfolioResponse = policyService.getPolicyNotifyPortfolio(userId, policyNo, currency);
            // 若無資料，嘗試由內部服務取得資料
            if (portfolioResponse != null) {
                logger.info("Get user[{}] data from eservice_api[getPolicyloanByPolicyNo]", userId);
                List<PortfolioVo> portfolioList = portfolioResponse.getPortfolioList();
                if (CollectionUtils.isNotEmpty(portfolioList)) {
                    for (PortfolioVo portfolioVo : portfolioList) {
                        NotifyConfigVo configVo = policyService.getNotifyConfig(getLoginUser().getId(), policyNo, portfolioVo.getInvtNo());
                        if (configVo == null) {
                            configVo = new NotifyConfigVo();
                        }
                        BeanUtils.copyProperties(portfolioVo, configVo);
                        result.add(configVo);
                    }
                }
            }
            processSuccess(result);
        } catch (Exception e) {
            logger.error("Unable to getPortfolioList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }
}
