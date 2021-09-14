package com.twfhclife.eservice.policy.service.impl;

import com.twfhclife.eservice.policy.dao.PolicyReversalDao;
import com.twfhclife.eservice.policy.model.PolicyReversalVo;
import com.twfhclife.eservice.policy.service.IPolicyReversalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyReversalServiceImpl implements IPolicyReversalService {

    @Autowired
    private PolicyReversalDao policyReversalDao;

    @Override
    public List<PolicyReversalVo> findByPolicyNo(String policyNo) {
        return policyReversalDao.findByPolicyNo(policyNo);
    }
}
