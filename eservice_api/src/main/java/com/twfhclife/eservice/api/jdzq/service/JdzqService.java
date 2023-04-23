package com.twfhclife.eservice.api.jdzq.service;


import com.twfhclife.eservice.api.jdzq.dao.JdzqDao;
import com.twfhclife.eservice.api.jdzq.dao.JdzqPolicyDao;
import com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest;
import com.twfhclife.eservice.api.jdzq.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JdzqService {

    @Autowired
    private JdzqDao jdzqDao;

    @Autowired
    private JdzqPolicyDao jdzqPolicyDao;

    public List<CaseVo> getPersonalCaseList(CaseQueryRequest caseQuery) {
        return jdzqDao.getPersonalCaseList(caseQuery);
    }

    public CaseVo getCaseProcess(String policyNo) {
        return jdzqDao.getCaseProcess(policyNo);
    }

    public CaseVo getPolicyInfo(String policyNo) {
        return jdzqDao.getPolicyInfo(policyNo);
    }

    public List<CaseVo> getNoteContent(String policyNo) {
        return jdzqDao.getNoteContent(policyNo);
    }

    public NotePdfVo getNotePdf(String policyNo) {
        return jdzqDao.getNotePdf(policyNo);
    }

    public List<NoteNotifyVo> getNoteSchedule() {
        return jdzqDao.getNoteSchedule(new Date());
    }

    public List<PolicyClaimDetailVo> getPolicyTypeNameList(PolicyClaimDetailVo vo){
        return jdzqPolicyDao.getPolicyTypeNameList(vo);
    }

    public List<PolicyClaimDetailVo> getPolicyClaimDetail(PolicyClaimReqVo vo){
        String str = "";
        List<String> columns = vo.getColumn();
        int k = columns.size();
        for (int i = 0; i < k; i++) {
            if (columns.get(i).equals("policyNoteContent")){
                str += "ff.noteDate,ff.dueDate,ff.itemContent,ff.contentMemo";
            }else {
                str += "ff." + columns.get(i);
            }
            if(i != k-1) {
                str += ",";
            }
        }
        return jdzqPolicyDao.getPolicyClaimDetail(vo,str);
    }

    public List<CaseVo> getCaseList(CaseQueryRequest caseQuery) {
        return jdzqDao.getCaseList(caseQuery);
    }

    public int getCaseTotal(CaseQueryRequest caseQuery) {
        return jdzqDao.getCaseTotal(caseQuery);
    }
}
