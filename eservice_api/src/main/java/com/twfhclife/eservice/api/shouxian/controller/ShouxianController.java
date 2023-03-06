package com.twfhclife.eservice.api.shouxian.controller;

import com.twfhclife.eservice.api.elife.domain.*;
import com.twfhclife.eservice.api.shouxian.domain.*;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.api.shouxian.service.ShouxianService;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
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

@RestController
public class ShouxianController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShouxianController.class);

    @Autowired
    private ShouxianService shouxianService;

    @PostMapping(value = "/getPolicyList", produces = { "application/json" })
    public ResponseEntity<?> getPolicyList(@RequestBody PolicyVo vo) {
        ApiResponseObj<PolicyListDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<PolicyVo> policyList = shouxianService.getPolicyList(vo);
            PolicyListDataResponse resp = new PolicyListDataResponse();
            resp.setPolicyList(policyList);
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
    public ResponseEntity<?> getPolicySafeGuard(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicySafeGuardDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicySafeGuardVo policySafeGuardVo = shouxianService.getSafeGuard(vo.getPolicyNo());
            PolicySafeGuardDataResponse resp = new PolicySafeGuardDataResponse();
            resp.setSafeGuardVo(policySafeGuardVo);
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

    @PostMapping(value = "/getPaymentRecord", produces = { "application/json" })
    public ResponseEntity<?> getPaymentRecord(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyPaymentRecordDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyPaymentRecordVo policyPaymentRecordVo = shouxianService.getPaymentRecord(vo.getPolicyNo());
            PolicyPaymentRecordDataResponse resp = new PolicyPaymentRecordDataResponse();
            resp.setPolicyPaymentRecord(policyPaymentRecordVo);
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
    public ResponseEntity<?> getPolicyPremium(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyPremiumDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyPremiumVo policyPremiumVo = shouxianService.getPolicyPremium(vo.getPolicyNo());
            PolicyPremiumDataResponse resp = new PolicyPremiumDataResponse();
            resp.setPolicyPremium(policyPremiumVo);
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
    public ResponseEntity<?> getPolicyExpireOfPayment(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyExpireOfPaymentDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyExpireOfPaymentVo policyExpireOfPaymentVo = shouxianService.getExpireOfPayment(vo.getPolicyNo());
            PolicyExpireOfPaymentDataResponse resp = new PolicyExpireOfPaymentDataResponse();
            resp.setPolicyExpireOfPayment(policyExpireOfPaymentVo);
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
    public ResponseEntity<?> getPolicyChangeInfo(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyChangeInfoDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyChangeInfoVo policyChangeInfoVo = shouxianService.getPolicyChangeInfo(vo.getPolicyNo());
            PolicyChangeInfoDataResponse resp = new PolicyChangeInfoDataResponse();
            resp.setChangeInfo(policyChangeInfoVo);
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
    public ResponseEntity<?> getIncomeDistribution(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyIncomeDistributionDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            PolicyIncomeDistributionVo policyIncomeDistributionVo = shouxianService.getPolicyIncomeDistribution(vo.getPolicyNo());
            PolicyIncomeDistributionDataResponse resp = new PolicyIncomeDistributionDataResponse();
            resp.setIncomeDistribution(policyIncomeDistributionVo);
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
    public ResponseEntity<?> getPolicyTransactionHistory(
            @Valid @RequestBody PolicyFundTransactionRequest req) {
        ApiResponseObj<PolicyFundTransactionResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        PolicyFundTransactionResponse resp = new PolicyFundTransactionResponse();

        try {
            String policyNo = req.getPolicyNo();
            String transType = req.getTransType();
            String startDate = req.getStartDate();
            String endDate = req.getEndDate();
            Integer pageNum = req.getPageNum();
            Integer pageSize = req.getPageSize();

            if (!StringUtils.isEmpty(policyNo)) {
                int total = shouxianService.
                        getFundTransactionTotal(policyNo, transType, startDate, endDate);
                List<FundTransactionVo> fundTransactionList = shouxianService.
                        getFundTransactionPageList(policyNo, transType, startDate, endDate, pageNum, pageSize);
                for (FundTransactionVo fundTransactionVo : fundTransactionList) {
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


    @PostMapping(value = "/getPolicyPortfolioNew", produces = { "application/json" })
    public ResponseEntity<?> getPolicyPortfolioNew(@Valid @RequestBody PortfolioRequest req) {
        ApiResponseObj<PortfolioResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        PortfolioResponse resp = new PortfolioResponse();

        try {
            String policyNo = req.getPolicyNo();
            if (!StringUtils.isEmpty(policyNo)) {
                List<PortfolioVo> portfolioList = shouxianService.getPortfolioList(policyNo);
                resp.setPortfolioList(portfolioList);
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

    @PostMapping(value = "/getPolicyCancellationMoney", produces = { "application/json" })
    public ResponseEntity<?> getPolicyCancellationMoney(@RequestBody PolicyBaseVo vo) {
        ApiResponseObj<PolicyCancellationMoneyDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CancellationMoneyVo> cancellationMoneyVos = shouxianService.getPolicyCancellationMoney(vo.getPolicyNo());
            PolicyCancellationMoneyDataResponse resp = new PolicyCancellationMoneyDataResponse();
            resp.setCancellationMoneyVos(cancellationMoneyVos);
            resp.setPolicyVo(shouxianService.getPolicyInfo(vo.getPolicyNo()));
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
}
