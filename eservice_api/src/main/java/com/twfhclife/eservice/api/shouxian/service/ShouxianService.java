package com.twfhclife.eservice.api.shouxian.service;


import com.twfhclife.eservice.api.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.api.shouxian.model.PolicyBaseVo;
import com.twfhclife.eservice.api.shouxian.model.PolicySafeGuardVo;
import com.twfhclife.eservice.api.shouxian.model.PolicyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShouxianService {

    @Autowired
    private ShouXianDao shouXianDao;

    public List<PolicyVo> getPolicyList(PolicyVo vo) {
        return shouXianDao.getPolicyList(vo);
    }

    public PolicyBaseVo getPolicyBase(String policyNo) {
        return shouXianDao.getBasePolicy(policyNo);
    }

    public PolicySafeGuardVo getSafeGuard(String policyNo) {
        return shouXianDao.getSafeGuard(policyNo);
    }
}
