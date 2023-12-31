package com.twfhclife.eservice.api.jdzq.dao;

import com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import com.twfhclife.eservice.api.jdzq.model.NoteNotifyVo;
import com.twfhclife.eservice.api.jdzq.model.NotePdfVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface JdzqDao {

    List<CaseVo> getPersonalCaseList(@Param("vo") CaseQueryRequest caseQuery);
    CaseVo getCaseProcess(@Param("policyNo") String policyNo);
    CaseVo getPolicyInfo(@Param("policyNo") String policyNo);
    List<CaseVo> getNoteContent(@Param("policyNo") String policyNo);
    NotePdfVo getNotePdf(@Param("policyNo") String policyNo, @Param("noteKey") String noteKey);
    List<NoteNotifyVo> getNoteSchedule(@Param("currDate") Date date);

    int getCaseTotal(@Param("vo") CaseQueryRequest caseQuery);

    List<CaseVo> getCaseList(@Param("vo") CaseQueryRequest caseQuery);

//    List<PolicyClaimDetailVo> getPolicyClaimDetail(@Param("vo") PolicyClaimDetailVo vo,@Param("columnItem")String columnItem);
}
