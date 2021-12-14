package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import org.apache.ibatis.annotations.Param;

public interface TransCashPaymentDao {

    int insert(TransCashPaymentVo record);

    TransCashPaymentVo getCashPaymentByPolicyNo(@Param("policyNo") String policyNo);

    TransCashPaymentVo getTransPaymentByTransNum(@Param("transNum")  String transNum);

    int countTransBankInfo(@Param("policyNo") String userId);
}