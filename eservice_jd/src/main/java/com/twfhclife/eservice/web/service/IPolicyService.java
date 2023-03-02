package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.api_model.PolicyFundTransactionResponse;
import com.twfhclife.eservice.api_model.PolicyPremiumCostResponse;
import com.twfhclife.eservice.api_model.PolicyPremiumTransactionResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.model.PolicyChangeInfoVo;
import com.twfhclife.eservice.web.model.*;

import java.util.List;

public interface IPolicyService {

    List<PolicyVo> getPolicyList(KeycloakUser loginUser, PolicyVo vo);

    PolicyBaseVo getPolicyBase(String policyNo);

    PolicySafeGuardVo getPolicyGuard(String policyNo);

    PolicyPaymentRecordVo getPolicyPaymentRecord(String policyNo);

    PolicyPremiumVo getPolicyPremium(String policyNo);

    PolicyExpireOfPaymentVo getPolicyExpireOfPayment(String policyNo);

    PolicyChangeInfoVo getChangeInfo(String policyNo);

    PolicyIncomeDistributionVo getIncomeDistribution(String policyNo);

    PolicyFundTransactionResponse getPolicyFundTransaction(String userId, String policyNo, String trCode, String startDate, String endDate, int pageNum, int defaultPageSize);

    PolicyPremiumCostResponse getPolicyPremiumTransaction(String userId, String policyNo, String startDate, String endDate, int pageNum, int defaultPageSize);
}
