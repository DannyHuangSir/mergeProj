package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;

public interface ITransCashPaymentService {

    int insertCashPayment(TransCashPaymentVo vo, String userId);

    String getPreAllocation(String policyNo);

    TransCashPaymentVo getCurrentTransCashPayment(String transNum);

    Boolean checkHasBankInfo(String userId);
}
