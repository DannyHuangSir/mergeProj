package com.twfhclife.eservice.api.jdzq.controller;

import com.twfhclife.eservice.api.jdzq.domain.PersonalCaseDataResponse;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import com.twfhclife.eservice.api.jdzq.service.JdzqService;
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
public class JdzqController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(JdzqController.class);

    @Autowired
    private JdzqService jdzqService;

    @PostMapping(value = "/getPersonalCaseList", produces = { "application/json" })
    public ResponseEntity<?> getPersonalCaseList(@RequestBody List<String> serialNums) {
        ApiResponseObj<PersonalCaseDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CaseVo> caseList = jdzqService.getCaseList(serialNums);
            PersonalCaseDataResponse resp = new PersonalCaseDataResponse();
            resp.setCaseList(caseList);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getPersonalCaseList: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }
}
