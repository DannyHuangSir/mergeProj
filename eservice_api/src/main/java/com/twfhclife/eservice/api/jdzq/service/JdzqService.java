package com.twfhclife.eservice.api.jdzq.service;


import com.twfhclife.eservice.api.jdzq.dao.JdzqDao;
import com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import com.twfhclife.eservice.web.model.PolicyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdzqService {

    @Autowired
    private JdzqDao jdzqDao;

    public List<CaseVo> getCaseList(CaseQueryRequest caseQuery) {
        return jdzqDao.getCaseList(caseQuery);
    }

    public CaseVo getCaseProcess(PolicyVo policyVo) {
        return jdzqDao.getCaseProcess(policyVo.getPolicyNo());
    }

    public CaseVo getPolicyInfo(PolicyVo policyVo) {
        return jdzqDao.getPolicyInfo(policyVo.getPolicyNo());
    }
}
