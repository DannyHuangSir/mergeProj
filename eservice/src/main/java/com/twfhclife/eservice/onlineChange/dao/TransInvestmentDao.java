package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.policy.model.CompareInvestmentVo;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransInvestmentDao {

    int insert(TransInvestmentVo record);

    List<CompareInvestmentVo> selectCompareInvestments(@Param("transNum") String transNum);

    String selectProcessInvestment(@Param("userId") String userId, @Param("transTypes") List<String> transTypes);

    List<InvestmentPortfolioVo> getOwnInvestment(@Param("policyNo") String policyNo, @Param("date")Date date);

    List<InvestmentPortfolioVo> getNewInvestments(@Param("policyNo") String policyNo, @Param("ownInvtNos")  List<String> invtNos, @Param("rrs") List<String> ownInvtNos);

    List<InvestmentPortfolioVo> getHeldInvestmentTarget(@Param("policyNo") String policyNo);

    BigDecimal getDistributeRationByInvtNo(@Param("policyNo") String policyNo, @Param("invtNo") String invtNo, @Param("date")Date date);
}