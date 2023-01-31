package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.web.model.PolicyListVo;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyServiceImpl implements IPolicyService {

    @Autowired
    private ShouXianDao shouXianDao;

    @Override
    public List<PolicyListVo> getPolicyList() {
        return shouXianDao.getPolicyList();
    }
}
