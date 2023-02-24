package com.twfhclife.eservice.api.shouxian.controller;

import com.twfhclife.eservice.api.shouxian.domain.PolicyBaseDataResponse;
import com.twfhclife.eservice.api.shouxian.domain.PolicyListDataResponse;
import com.twfhclife.eservice.api.shouxian.domain.PolicySafeGuardDataResponse;
import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;
import com.twfhclife.eservice.api.shouxian.model.PolicySafeGuardVo;
import com.twfhclife.eservice.api.shouxian.model.PolicyVo;
import com.twfhclife.eservice.api.shouxian.service.ShouxianService;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
