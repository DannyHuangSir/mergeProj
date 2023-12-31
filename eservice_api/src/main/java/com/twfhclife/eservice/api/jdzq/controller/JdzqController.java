package com.twfhclife.eservice.api.jdzq.controller;

import com.twfhclife.eservice.api.jdzq.domain.*;
import com.twfhclife.eservice.api.jdzq.model.*;
import com.twfhclife.eservice.api.jdzq.service.JdzqService;
import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;
import com.twfhclife.generic.annotation.*;
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


    @ApiRequest
    @PostMapping(value = "/getCaseProcess", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-002",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getCaseProcess",
                            execMethod = "經代案件進度查詢",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                            }
                    )}))
    public ResponseEntity<?> getCaseProcess(@RequestBody PolicyBaseVo policyVo) {
        ApiResponseObj<CaseProcessDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            CaseVo caseVo = jdzqService.getCaseProcess(policyVo.getPolicyNo());
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

    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-001",
            systemEventParams = {
                    @SystemEventParam(
                            sqlVoType = "com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest",
                            sqlVoKey = "vo",
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getPersonalCaseList",
                            execMethod= "經代个人案件列表查詢"
                    )
            }
    ))
    @PostMapping(value = "/getPersonalCaseList", produces = { "application/json" })
    @ApiRequest
    public ResponseEntity<?> getPersonalCaseList(@RequestBody CaseQueryRequest caseQuery) {
        ApiResponseObj<PersonalCaseDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CaseVo> caseList = jdzqService.getPersonalCaseList(caseQuery);
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

    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-020",
            systemEventParams = {
                    @SystemEventParam(
                            sqlVoType = "com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest",
                            sqlVoKey = "vo",
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getCaseList",
                            execMethod= "經代案件列表查詢"
                    )
            }
    ))
    @PostMapping(value = "/getCaseList", produces = { "application/json" })
    @ApiRequest
    public ResponseEntity<?> getCaseList(@RequestBody CaseQueryRequest caseQuery) {
        ApiResponseObj<CaseListDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            CaseListDataResponse resp = new CaseListDataResponse();
            resp.setRows(jdzqService.getCaseList(caseQuery));
            resp.setTotalRow(jdzqService.getCaseTotal(caseQuery));
            resp.setPageSize(caseQuery.getPageSize());
            resp.setPageNum(caseQuery.getPageNum());
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        } catch (Exception e) {
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to getCaseList: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        } finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @ApiRequest
    @PostMapping(value = "/jdzqGetPolicyInfo", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-003",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getPolicyInfo",
                            execMethod = "經代保單資訊查詢",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                            }
                    )}))
    public ResponseEntity<?> jdzqGetPolicyInfo(@RequestBody PolicyBaseVo policyVo) {
        ApiResponseObj<CaseProcessDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            CaseVo caseVo = jdzqService.getPolicyInfo(policyVo.getPolicyNo());
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

    @ApiRequest
    @PostMapping(value = "/getNoteContent", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-004",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getNoteContent",
                            execMethod = "經代照會查詢",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                            }
                    )}))
    public ResponseEntity<?> getNoteContent(@RequestBody PolicyBaseVo policyVo) {
        ApiResponseObj<NoteContentDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<CaseVo> caseVo = jdzqService.getNoteContent(policyVo.getPolicyNo());
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

    @PostMapping(value = "/jdGetPolicyClaimDetail", produces = { "application/json" })
    public ResponseEntity<?> jdGetPolicyClaimDetail(@RequestBody PolicyClaimReqVo vo){
        ApiResponseObj<PolicyClaimDetailResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<PolicyClaimDetailVo> policyClaimDetail = jdzqService.getPolicyClaimDetail(vo);
            PolicyClaimDetailResponse resp = new PolicyClaimDetailResponse();
            resp.setPolicyClaimDetailVo(policyClaimDetail);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        }catch (Exception e){
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to jdGetPolicyClaimDetail: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        }finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @PostMapping(value = "/jdGetUserDetail", produces = { "application/json" })
    public ResponseEntity<?> jdGetUserDetail(@RequestBody UserDetailReqVo vo){
        ApiResponseObj<UserDetailResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<UserDetailVo>  policyClaimDetails = jdzqService.getUserDetail(vo);
            UserDetailResponse resp = new UserDetailResponse();
            resp.setUserDetailVos(policyClaimDetails);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        }catch (Exception e){
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to jdGetUserDetail: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        }finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }

    @ApiRequest
    @PostMapping(value = "/getNotePdf", produces = { "application/json" })
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "JD-005",
            systemEventParams = {
                    @SystemEventParam(
                            sqlId = "com.twfhclife.eservice.api.jdzq.dao.JdzqDao.getNotePdf",
                            execMethod = "經代照會單",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "policyNo", sqlParamkey = "policyNo"),
                            }
                    )}))
    public ResponseEntity<?> getNotePdf(@RequestBody PdfVo pdfVo) {
        ApiResponseObj<NotePdfDataResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            NotePdfVo notePdf = jdzqService.getNotePdf(pdfVo.getPolicyNo(), pdfVo.getNoteKey());
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

    @PostMapping(value = "/jdGetPolicyTypeNameList", produces = { "application/json" })
    public ResponseEntity<?> jdGetPolicyTypeNameList(@RequestBody PolicyClaimDetailVo vo){
        ApiResponseObj<PolicyClaimDetailResponse> apiResponseObj = new ApiResponseObj<>();
        ReturnHeader returnHeader = new ReturnHeader();
        try {
            List<PolicyClaimDetailVo> policyClaimDetail = jdzqService.getPolicyTypeNameList(vo);
            PolicyClaimDetailResponse resp = new PolicyClaimDetailResponse();
            resp.setPolicyClaimDetailVo(policyClaimDetail);
            returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
            apiResponseObj.setReturnHeader(returnHeader);
            apiResponseObj.setResult(resp);
        }catch (Exception e){
            returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
            logger.error("Unable to jdGetPolicyTypeNameList: {}", ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
        }finally {
            apiResponseObj.setReturnHeader(returnHeader);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
    }
}
