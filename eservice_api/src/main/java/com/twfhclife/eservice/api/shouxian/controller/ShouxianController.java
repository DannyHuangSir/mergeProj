package com.twfhclife.eservice.api.shouxian.controller;

import com.twfhclife.eservice.api.elife.domain.PolicyFundTransactionRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyPremiumTransactionRequest;
import com.twfhclife.eservice.api.elife.domain.PortfolioRequest;
import com.twfhclife.eservice.api.elife.domain.PortfolioResponse;
import com.twfhclife.eservice.api.shouxian.domain.*;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.api.shouxian.service.ShouxianService;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.*;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.service.IOptionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ShouxianController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShouxianController.class);

    @Autowired
    private ShouxianService shouxianService;

    @PostMapping(value = "/getPolicyList", produces = { "application/json" })
    @ApiRequest
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-006",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPolicyList",
                            execMethod = "保單查詢",
                            sqlVoType = "com.twfhclife.eservice.api.shouxian.model.PolicyVo",
                            sqlVoKey = "vo"
                    )
            }))
    public ResponseEntity<?> getPolicyList(@RequestBody PolicyVo vo) {
        ApiResponseObj<PolicyListDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyListDataResponse resp = new PolicyListDataResponse();
            PolicyCountVo countVo = shouxianService.getPolicyListTotal(vo);
            resp.setTotalRow(countVo == null ? 0 : countVo.getTotal());
            resp.setPolicyCountVo(countVo);
            resp.setRows(shouxianService.getPolicyList(vo));
            resp.setPageNum(vo.getPageNum());
            resp.setPageSize(vo.getPageSize());
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyList: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyBase", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-007",
            systemEventParams = {
            @SystemEventParam(
                    sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getBasePolicy",
                    execMethod = "保單基本資料",
                    sqlParams = {
                            @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                    }
            )}))
    public ResponseEntity<?> getPolicyBase(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyBaseDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyBaseVo policyBase = shouxianService.getPolicyBase(vo.getPolicyNo());
            PolicyBaseDataResponse resp = new PolicyBaseDataResponse();
            resp.setPolicy(policyBase);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyBase: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicySafeGuard", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-008",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getSafeGuard",
                            execMethod = "經代保障內容與金額",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicySafeGuard(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicySafeGuardDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicySafeGuardDataResponse resp = new PolicySafeGuardDataResponse();
            resp.setSafeGuard(shouxianService.getSafeGuard(vo.getPolicyNo()));
            resp.setPolicyBase(shouxianService.getPolicyBase(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicySafeGuard: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @Autowired
    private IOptionService optionService;
    
    @PostMapping(value = "/getPaymentRecord", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-009",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPaymentRecord",
                            execMethod = "經代繳費紀錄",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPaymentRecord(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyPaymentRecordDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyPaymentRecordDataResponse resp = new PolicyPaymentRecordDataResponse();
            PolicyBaseVo policyBase = shouxianService.getPolicyBase(vo.getPolicyNo());
            List<Map<String, Object>> filter = optionService.getBankList().stream()
                    .filter(f -> StringUtils.equals(String.valueOf(f.get("key")),  policyBase.getBankCode())).collect(Collectors.toList());
            if (filter.size() > 0) {
                policyBase.setBankCode(String.valueOf(filter.get(0).get("value")));
            }
            resp.setPolicyBaseVo(policyBase);
            resp.setPaymentRecords(shouxianService.getPaymentRecord(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPaymentRecord: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyPremium", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-010",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPolicyPremium",
                            execMethod = "經代保單紅利資料",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicyPremium(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyPremiumDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyPremiumDataResponse resp = new PolicyPremiumDataResponse();
            resp.setPolicyBase(shouxianService.getPolicyBase(vo.getPolicyNo()));
            resp.setPremiums(shouxianService.getPolicyPremium(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyPremium: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyExpireOfPayment", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-011",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getExpireOfPayment",
                            execMethod = "經代生存/滿期給付資料",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicyExpireOfPayment(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyExpireOfPaymentDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyExpireOfPaymentDataResponse resp = new PolicyExpireOfPaymentDataResponse();
            resp.setPolicyBase(shouxianService.getPolicyBase(vo.getPolicyNo()));
            resp.setPayments(shouxianService.getExpireOfPayment(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyExpireOfPayment: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyChangeInfo", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-012",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPolicyChangeInfo",
                            execMethod = "經代契約變更資料",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicyChangeInfo(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyChangeInfoDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyChangeInfoDataResponse resp = new PolicyChangeInfoDataResponse();
            resp.setPolicyBase(shouxianService.getPolicyBase(vo.getPolicyNo()));
            resp.setChangeInfos(shouxianService.getPolicyChangeInfo(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyChangeInfo: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getIncomeDistribution", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-013",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getIncomeDistribution",
                            execMethod = "經代保單收益分配",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getIncomeDistribution(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyIncomeDistributionDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyIncomeDistributionDataResponse resp = new PolicyIncomeDistributionDataResponse();
            resp.setPolicyBase(shouxianService.getPolicyBase(vo.getPolicyNo()));
            resp.setIncomeDistributions(shouxianService.getPolicyIncomeDistribution(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getIncomeDistribution: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyTransactionHistory", produces = {"application/json"})
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-014",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getFundTransactionPageList",
                            execMethod = "經代交易歷史記錄",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                                    @SqlParam(requestParamkey = "transType", sqlParamkey = "transType"),
                                    @SqlParam(requestParamkey = "pageNum", sqlParamkey = "pageNum"),
                                    @SqlParam(requestParamkey = "pageSize", sqlParamkey = "pageSize")
                            }
                    )}))
    public ResponseEntity<?> getPolicyTransactionHistory(
            @Valid @RequestBody PolicyFundTransactionRequest req) {
        ApiResponseObj<JdPolicyFundTransactionResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        JdPolicyFundTransactionResponse resp = new JdPolicyFundTransactionResponse();

        try {
            String policyNo = req.getPolicyNo();
            String transType = req.getTransType();
            String currency = req.getCurrency();
            String startDate = req.getStartDate();
            String endDate = req.getEndDate();
            Integer pageNum = req.getPageNum();
            Integer pageSize = req.getPageSize();

            if (!StringUtils.isEmpty(policyNo) && !StringUtils.isEmpty(currency)) {
                int total = shouxianService.
                        getFundTransactionTotal(policyNo, transType, currency, startDate, endDate);
                List<JdFundTransactionVo> fundTransactionList = shouxianService.
                        getFundTransactionPageList(policyNo, transType, currency, startDate, endDate, pageNum, pageSize);
                for (JdFundTransactionVo fundTransactionVo : fundTransactionList) {
                    fundTransactionVo.setPageNum(req.getPageNum());
                    fundTransactionVo.setPageSize(req.getPageSize());
                    fundTransactionVo.setTotalRow(total);
                }
                resp.setFundTransactionList(fundTransactionList);
                returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
                apiResponseObj.setReturnHeader(returnHeader);
                apiResponseObj.setResult(resp);
            }
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyFundTransaction: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @ApiRequest
    @PostMapping(value = "/getPolicyPremiumCost", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-015",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getFundPrdtPageList",
                            execMethod = "經代保費費用",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                                    @SqlParam(requestParamkey = "startDate", sqlParamkey = "startDate"),
                                    @SqlParam(requestParamkey = "endDate", sqlParamkey = "endDate"),
                                    @SqlParam(requestParamkey = "pageNum", sqlParamkey = "pageNum"),
                                    @SqlParam(requestParamkey = "pageSize", sqlParamkey = "pageSize")
                            }
                    )}))
    public ResponseEntity<?> getPolicyPremiumCost(
            @Valid @RequestBody PolicyPremiumTransactionRequest req) {
        ApiResponseObj<PolicyPremiumCostResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        PolicyPremiumCostResponse resp = new PolicyPremiumCostResponse();

        try {
            String policyNo = req.getPolicyNo();
            String startDate = req.getStartDate();
            String endDate = req.getEndDate();
            Integer pageNum = req.getPageNum();
            Integer pageSize = req.getPageSize();

            if (!StringUtils.isEmpty(policyNo)) {
                int total = shouxianService.getFundPrdtTotal(policyNo, startDate, endDate);
                List<FundPrdtVo> premiumTransactionList = shouxianService.
                        getFundPrdtPageList(policyNo, startDate, endDate, pageNum, pageSize);
                for (FundPrdtVo fundPrdtVo : premiumTransactionList) {
                    fundPrdtVo.setPageNum(req.getPageNum());
                    fundPrdtVo.setPageSize(req.getPageSize());
                    fundPrdtVo.setTotalRow(total);
                }
                resp.setPremiumTransactionList(premiumTransactionList);

                returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
                apiResponseObj.setReturnHeader(returnHeader);
                apiResponseObj.setResult(resp);
            }
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyPremiumCost: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyInvtFund", produces = { "application/json" })
    public ResponseEntity<?> getPolicyInvtFund(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyInvtFundDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyInvtFundVo policyInvtFund = shouxianService.getPolicyInvtFund(vo.getPolicyNo());
            PolicyInvtFundDataResponse resp = new PolicyInvtFundDataResponse();
            resp.setInvtFund(policyInvtFund);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyInvtFund: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyNotifyPortfolio", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-016",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPortfolioList",
                            execMethod = "經代報酬率通知設定",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicyNotifyPortfolio(@Valid @RequestBody PortfolioRequest req) {
        ApiResponseObj<PortfolioResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            String policyNo = req.getPolicyNo();
            String currency = req.getCurrency();
            if (!StringUtils.isEmpty(policyNo)) {
                PortfolioResponse resp = shouxianService.getPortfolioResp(policyNo, currency);
                returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
                apiResponseObj.setReturnHeader(returnHeader);
                apiResponseObj.setResult(resp);
            }
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyNotifyPortfolio: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyPortfolioNew", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-017",
            systemEventParams = {
            @SystemEventParam(
                    sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getPortfolioList",
                    execMethod = "經代投資損益及投報率",
                    sqlParams = {
                            @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                    }
            )}))
    public ResponseEntity<?> getPolicyPortfolioNew(@Valid @RequestBody PortfolioRequest req) {
        ApiResponseObj<PortfolioResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            String policyNo = req.getPolicyNo();
            String currency = req.getCurrency();
            if (!StringUtils.isEmpty(policyNo)) {
                PortfolioResponse resp = shouxianService.getPortfolioResp(policyNo, currency);
                returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
                apiResponseObj.setReturnHeader(returnHeader);
                apiResponseObj.setResult(resp);
            }
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyPortfolioNew: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @Autowired
    private IParameterService parameterService;

    @PostMapping(value = "/getPolicyCancellationMoney", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-018",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.selectPolicyAmount",
                            execMethod = "經代解約金試算",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo")
                            }
                    )}))
    public ResponseEntity<?> getPolicyCancellationMoney(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyCancellationMoneyDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyCancellationMoneyDataResponse resp = new PolicyCancellationMoneyDataResponse();
            List cancellationMoneyVos;
            String investParam = parameterService.getParameterValueByCode("eservice_jd", "JD_POLICY_INVESTMENT_TYPE");
            String investNewParam = parameterService.getParameterValueByCode("eservice_jd", "JD_NEW_POLICY_INVESTMENT_TYPE");
            if (StringUtils.contains(investParam, StringUtils.substring(vo.getPolicyNo(), 0, 2)) ||
                StringUtils.contains(investNewParam, StringUtils.substring(vo.getPolicyNo(), 0, 2))) {
                resp.setInvest(true);
                cancellationMoneyVos = shouxianService.getInvestPolicyCancellationMoney(vo.getPolicyNo());
            } else {
                cancellationMoneyVos = shouxianService.getPolicyCancellationMoney(vo.getPolicyNo());
            }
            resp.setCancellationMoneyVos(cancellationMoneyVos);
            resp.setPolicyVo(shouxianService.getPolicyBase(vo.getPolicyNo()));
            resp.setPolicyAmountVo(shouxianService.getPolicyAmount(vo.getPolicyNo()));
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyCancellationMoney: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getExchangeRate", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-019",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.shouxian.dao.ShouXianDao.getExchangeRate",
                            execMethod = "經代投資標的淨值",
                            sqlVoKey = "exchangeRateVo",
                            sqlVoType = "com.twfhclife.eservice.api.shouxian.domain.ExchangeRateRequest",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "effectiveDate", sqlParamkey = "effectiveDate"),
                                    @SqlParam(requestParamkey = "queryType", sqlParamkey = "queryType"),
                            }
                    )}))
    public ResponseEntity<?> getExchangeRate(@Valid @RequestBody ExchangeRateRequest vo) {
        ApiResponseObj<ExchangeRateDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<ExchangeRateVo> cancellationMoneyVos = shouxianService.getExchangeRate(vo);
            ExchangeRateDataResponse resp = new ExchangeRateDataResponse();
            resp.setExchangeRates(cancellationMoneyVos);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getExchangeRate: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyPortfolioSchedule", produces = { "application/json" })
    public ResponseEntity<?> getPolicyPortfolioSchedule(@RequestBody PolicyBaseVo req) {
        ApiResponseObj<PortfolioResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            String policyNo = req.getPolicyNo();
            String currency = req.getCurrency();
            if (!StringUtils.isEmpty(policyNo)) {
                PortfolioResponse resp = shouxianService.getPortfolioResp(policyNo, currency);
                returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
                apiResponseObj.setReturnHeader(returnHeader);
                apiResponseObj.setResult(resp);
            }
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyPortfolioSchedule: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getPolicyTypeList", produces = { "application/json" })
    public ResponseEntity<?> getPolicyTypeList() {
        ApiResponseObj<PolicyTypeListResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyTypeListResponse resp = new PolicyTypeListResponse();
            resp.setPolicyTypeList(shouxianService.getPolicyTypeList());
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPolicyTypeList: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }
}
