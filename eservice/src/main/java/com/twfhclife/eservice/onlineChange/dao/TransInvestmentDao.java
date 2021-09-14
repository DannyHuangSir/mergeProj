package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.policy.model.CompareInvestmentVo;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransInvestmentDao {

    int insert(TransInvestmentVo record);

    List<CompareInvestmentVo> selectCompareInvestments(@Param("transNum") String transNum);

    String selectProcessInvestment(@Param("userId") String userId, @Param("transTypes") List<String> transTypes);

    List<InvestmentPortfolioVo> getOwnInvestment(String policyNo);

    List<InvestmentPortfolioVo> getNewInvestments(@Param("policyNo") String policyNo, @Param("riskLevel") String riskLevel, @Param("ownInvtNos") List<String> ownInvtNos);

    List<InvestmentPortfolioVo> getHeldInvestmentTarget(@Param("policyNo") String policyNo);
}