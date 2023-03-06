package com.twfhclife.eservice.api.shouxian.dao;


import com.twfhclife.eservice.api.shouxian.domain.ExchangeRateRequest;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShouXianDao {

    List<PolicyVo> getPolicyList(@Param("vo") PolicyVo vo);

    PolicyBaseVo getBasePolicy(@Param("policyNo") String policyNo);
    PolicySafeGuardVo getSafeGuard(@Param("policyNo") String policyNo);
    PolicyPaymentRecordVo getPaymentRecord(@Param("policyNo") String policyNo);
    PolicyPremiumVo getPolicyPremium(@Param("policyNo") String policyNo);
    PolicyExpireOfPaymentVo getExpireOfPayment(@Param("policyNo") String policyNo);
    PolicyChangeInfoVo getPolicyChangeInfo(@Param("policyNo") String policyNo);
    PolicyVo getPolicyInfo(@Param("policyNo") String policyNo);

    List<IncomeDistributionVo> getIncomeDistribution(@Param("policyNo") String policyNo);

    List<FundTransactionVo> getFundTransactionPageList(
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
}
