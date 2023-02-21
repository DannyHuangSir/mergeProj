package com.twfhclife.eservice.api.shouxian.service;


import com.twfhclife.eservice.api.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.api.shouxian.model.PolicyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShouxianService {

    @Autowired
    private ShouXianDao shouXianDao;

    public List<PolicyVo> getPolicyList() {
        return shouXianDao.getPolicyList();
    }
}
