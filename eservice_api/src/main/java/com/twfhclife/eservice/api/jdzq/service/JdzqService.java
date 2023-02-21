package com.twfhclife.eservice.api.jdzq.service;


import com.twfhclife.eservice.api.jdzq.dao.JdzqDao;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdzqService {

    @Autowired
    private JdzqDao jdzqDao;

    public List<CaseVo> getCaseList(List<String> serialNums) {
        return jdzqDao.getCaseListBySerialNum(serialNums);
    }
}
