package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.api_model.*;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.model.PolicyChangeInfoVo;
import com.twfhclife.eservice.web.model.*;

import java.util.List;

public interface IPolicyService {

    List<PolicyVo> getPolicyList(KeycloakUser loginUser, PolicyVo vo);

    PolicyBaseVo getPolicyBase(String userId, String policyNo);

    PolicySafeGuardDataResponse getPolicyGuard(String userId, String policyNo);

    PolicyPaymentRecordVo getPolicyPaymentRecord(String userId, String policyNo);

    PolicyPremiumVo getPolicyPremium(String userId, String policyNo);

    PolicyExpireOfPaymentVo getPolicyExpireOfPayment(String userId, String policyNo);

    PolicyChangeInfoVo getChangeInfo(String userId, String policyNo);

    PolicyIncomeDistributionVo getIncomeDistribution(String userId, String policyNo);

    JdPolicyFundTransactionResponse getPolicyFundTransaction(String userId, String policyNo, String trCode, String startDate, String endDate, int pageNum, int defaultPageSize);

    PolicyPremiumCostResponse getPolicyPremiumTransaction(String userId, String policyNo, String startDate, String endDate, int pageNum, int defaultPageSize);

    PolicyInvtFundVo getPolicyInvtFund(String userId, String policyNo);

    PortfolioResponse getPolicyRateOfReturn(String userId, String policyNo);

    PolicyCancellationMoneyDataResponse getPolicyCancellationMoney(String userId, String policyNo);

    int updateNotifyConfig(List<NotifyConfigVo> confs);

    NotifyConfigVo getNotifyConfig(String id, String policyNo, String invtNo);

    List<ExchangeRateVo> getExchangeRate(String userId, ExchangeRateVo exchangeRateVo);

    List<PortfolioVo> getPolicyRateSchedule(String policyNo);
}
