package com.twfhclife.eservice.api.shouxian.service;


import com.twfhclife.eservice.api.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.api.shouxian.model.*;
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

    public PolicyPaymentRecordVo getPaymentRecord(String policyNo) {
        return shouXianDao.getPaymentRecord(policyNo);
    }

    public PolicyPremiumVo getPolicyPremium(String policyNo) {
        return shouXianDao.getPolicyPremium(policyNo);
    }

    public PolicyExpireOfPaymentVo getExpireOfPayment(String policyNo) {
        return shouXianDao.getExpireOfPayment(policyNo);
    }
}
