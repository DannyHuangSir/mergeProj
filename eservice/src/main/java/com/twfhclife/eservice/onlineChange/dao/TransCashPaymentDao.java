package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

public interface TransCashPaymentDao {

    int delete(Long id);

    int insert(TransCashPaymentVo record);

    TransCashPaymentVo selectByPrimaryKey(Long id);

    int update(TransCashPaymentVo record);

    TransCashPaymentVo getCashPaymentByPolicyNo(@Param("policyNo") String policyNo);

    TransCashPaymentVo getTransPaymentByTransNum(@Param("transNum")  String transNum);

    int countTransBankInfo(@Param("userId") String userId);
}