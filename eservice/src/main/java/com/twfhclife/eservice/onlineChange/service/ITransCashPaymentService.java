package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

public interface ITransCashPaymentService {

    int insertCashPayment(TransCashPaymentVo vo, String userId);

    String getPreAllocation(String policyNo);

    TransCashPaymentVo getCurrentTransCashPayment(String transNum);

    Boolean checkHasBankInfo(String userId);
    
    void handlePolicyStatusLocked(String userRocId, List<PolicyListVo> policyList, String investmentParameterCode);

}
