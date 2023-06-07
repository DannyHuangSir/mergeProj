package com.twfhclife.jd.web.service;

import com.twfhclife.jd.api_model.*;
import com.twfhclife.jd.keycloak.model.KeycloakUser;
import com.twfhclife.jd.web.model.*;

import java.util.List;

public interface IPolicyService {

    PolicyListDataResponse getPolicyList(KeycloakUser loginUser, PolicyVo vo);

    PolicyBaseVo getPolicyBase(String userId, String policyNo);

    PolicySafeGuardDataResponse getPolicyGuard(String userId, String policyNo);

    PolicyPaymentRecordDataResponse getPolicyPaymentRecord(String userId, String policyNo);

    PolicyPremiumDataResponse getPolicyPremium(String userId, String policyNo);

    PolicyExpireOfPaymentDataResponse getPolicyExpireOfPayment(String userId, String policyNo);

    PolicyChangeInfoDataResponse getChangeInfo(String userId, String policyNo);

    PolicyIncomeDistributionDataResponse getIncomeDistribution(String userId, String policyNo);

    JdPolicyFundTransactionResponse getPolicyFundTransaction(String userId, String policyNo, String trCode, int pageNum, int defaultPageSize);

    PolicyPremiumCostResponse getPolicyPremiumTransaction(String userId, String policyNo, String startDate, String endDate, int pageNum, int defaultPageSize);

    PolicyInvtFundVo getPolicyInvtFund(String userId, String policyNo);

    PortfolioResponse getPolicyNotifyPortfolio(String userId, String policyNo);

    PortfolioResponse getPolicyRateOfReturn(String userId, String policyNo);

    PolicyCancellationMoneyDataResponse getPolicyCancellationMoney(String userId, String policyNo);

    int updateNotifyConfig(List<NotifyConfigVo> confs);

    NotifyConfigVo getNotifyConfig(String id, String policyNo, String invtNo);

    List<ExchangeRateVo> getExchangeRate(String userId, ExchangeRateVo exchangeRateVo);

    List<PortfolioVo> getPolicyRateSchedule(String policyNo);
}
