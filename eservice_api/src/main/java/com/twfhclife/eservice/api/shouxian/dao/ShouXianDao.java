package com.twfhclife.eservice.api.shouxian.dao;


import com.twfhclife.eservice.api.shouxian.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShouXianDao {

    List<PolicyVo> getPolicyList(@Param("vo") PolicyVo vo);

    PolicyBaseVo getBasePolicy(@Param("policyNo") String policyNo);
    PolicySafeGuardVo getSafeGuard(@Param("policyNo") String policyNo);
    PolicyPaymentRecordVo getPaymentRecord(@Param("policyNo") String policyNo);
    PolicyPremiumVo getPolicyPremium(@Param("policyNo") String policyNo);
    PolicyExpireOfPaymentVo getExpireOfPayment(@Param("policyNo") String policyNo);
}
