package com.twfhclife.eservice.api.jdzq.controller;

import com.twfhclife.eservice.api.jdzq.domain.*;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import com.twfhclife.eservice.api.jdzq.model.NoteNotifyVo;
import com.twfhclife.eservice.api.jdzq.model.NotePdfVo;
import com.twfhclife.eservice.api.jdzq.service.JdzqService;
import com.twfhclife.eservice.web.model.PolicyVo;
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
    public ResponseEntity<?> getPersonalCaseList(@RequestBody CaseQueryRequest caseQuery) {
        ApiResponseObj<PersonalCaseDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CaseVo> caseList = jdzqService.getCaseList(caseQuery);
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

    @PostMapping(value = "/getCaseProcess", produces = { "application/json" })
    public ResponseEntity<?> getCaseProcess(@RequestBody PolicyVo policyVo) {
        ApiResponseObj<CaseProcessDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            CaseVo caseVo = jdzqService.getCaseProcess(policyVo);
            CaseProcessDataResponse resp = new CaseProcessDataResponse();
            resp.setCaseVo(caseVo);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getCaseProcess: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/jdzqGetPolicyInfo", produces = { "application/json" })
    public ResponseEntity<?> jdzqGetPolicyInfo(@RequestBody PolicyVo policyVo) {
        ApiResponseObj<CaseProcessDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            CaseVo caseVo = jdzqService.getPolicyInfo(policyVo);
            CaseProcessDataResponse resp = new CaseProcessDataResponse();
            resp.setCaseVo(caseVo);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to jdzqGetPolicyInfo: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getNoteContent", produces = { "application/json" })
    public ResponseEntity<?> getNoteContent(@RequestBody PolicyVo policyVo) {
        ApiResponseObj<NoteContentDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CaseVo> caseVo = jdzqService.getNoteContent(policyVo);
            NoteContentDataResponse resp = new NoteContentDataResponse();
            resp.setCases(caseVo);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getNoteContent: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getNotePdf", produces = { "application/json" })
    public ResponseEntity<?> getNotePdf(@RequestBody PolicyVo policyVo) {
        ApiResponseObj<NotePdfDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            NotePdfVo notePdf = jdzqService.getNotePdf(policyVo);
            NotePdfDataResponse resp = new NotePdfDataResponse();
            resp.setNotePdf(notePdf);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getNotePdf: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/getNoteSchedule", produces = { "application/json" })
    public ResponseEntity<?> getNoteSchedule() {
        ApiResponseObj<NoteNotifyDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<NoteNotifyVo> noteNotifyVos = jdzqService.getNoteSchedule();
            NoteNotifyDataResponse resp = new NoteNotifyDataResponse();
            resp.setNoteNotifies(noteNotifyVos);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getNoteSchedule: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }
}
