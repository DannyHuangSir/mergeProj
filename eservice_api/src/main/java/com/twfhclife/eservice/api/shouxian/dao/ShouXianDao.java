package com.twfhclife.eservice.api.shouxian.dao;


import com.twfhclife.eservice.api.shouxian.domain.ExchangeRateRequest;
import com.twfhclife.eservice.api.shouxian.model.JdFundTransactionVo;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ShouXianDao {

    List<PolicyVo> getPolicyList(@Param("vo") PolicyVo vo);

    PolicyCountVo getPolicyListTotal(@Param("vo") PolicyVo vo);

    PolicyBaseVo getBasePolicy(@Param("policyNo") String policyNo, @Param("policyType") String policyType, @Param("policyGrpNo") String policyGrpNo, @Param("policySeqNo") String policySeqNo);
    List<SafeGuardVo> getSafeGuard(@Param("policyNo") String policyNo);
    List<PaymentRecordVo> getPaymentRecord(@Param("policyNo") String policyNo);
    List<PremiumVo> getPolicyPremium(@Param("policyNo") String policyNo);
    List<ExpireOfPaymentVo> getExpireOfPayment(@Param("policyNo") String policyNo);
    List<ChangeInfoVo> getPolicyChangeInfo(@Param("policyNo") String policyNo);

    List<IncomeDistributionVo> getIncomeDistribution(@Param("policyNo") String policyNo);

    List<JdFundTransactionVo> getFundTransactionPageList(
            @Param("policyNo") String policyNo,
            @Param("trCode") String trCode,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    int getFundTransactionTotal(
            @Param("policyNo") String policyNo,
            @Param("trCode") String trCode,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    List<FundPrdtVo> getFundPrdtPageList(@Param("policyNo") String policyNo,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("pageNum") Integer pageNum,
                                          @Param("pageSize") Integer pageSize);

    int getFundPrdtTotal(@Param("policyNo") String policyNo,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    List<PortfolioVo> getPortfolioList(@Param("policyNo") String policyNo);

    List<ExchangeRateVo> getExchangeRate(@Param("exchangeRateVo") ExchangeRateRequest vo);
    PolicyAmountVo selectPolicyAmount(@Param("policyNo") String policyNo);

    List<Map<String, String>> selectPolicyTypeList();
}
