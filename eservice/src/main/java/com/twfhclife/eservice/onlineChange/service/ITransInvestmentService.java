package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.TransFundConversionVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.policy.model.CompareInvestmentVo;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.UsersVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ITransInvestmentService {

    void handlePolicyStatusLocked(String userRocId, List<PolicyListVo> policyList, String investmentParameterCode);

    List<InvestmentPortfolioVo> getOwnInvestment(String policyNo);

    List<InvestmentPortfolioVo> getNewInvestments(String policyNo, List<InvestmentPortfolioVo> portfolioVos, String userId);

    List<CompareInvestmentVo> compareNew(List<InvestmentPortfolioVo> ownInvts, List<InvestmentPortfolioVo> newInvts);

    void addNewInvestmentApply(TransInvestmentVo vo, UsersVo user) throws Exception;

    /**
     * 计算 已持有投資標的轉換 数据
     * @param outInvestmentPortfolioVo  转出数据
     * @return
     */
    List<TransFundConversionVo> outransFundConversionDate(List<InvestmentPortfolioVo> outInvestmentPortfolioVo)throws Exception;;
    /**
     * 计算 已持有投資標的轉換 数据
     * @param inInvestmentPortfolioVo  转入数据
     * @return
     */
    List<TransFundConversionVo> inransFundConversionDate(List<InvestmentPortfolioVo> inInvestmentPortfolioVo)throws Exception;;

    /**
     * 添加 轉出轉入基金
     * @param outInvestments
     * @param inInvestments
     * @param user
     * @return
     */
    TransInvestmentVo insertTransFundConversion(TransInvestmentVo transInvestmentVo, UsersVo user)throws Exception;

    /***
     * 風險屬性轉換
     * @param riskLevel
     * @return
     */
    String transRiskLevelToName(String riskLevel);

    List<TransInvestmentDetailVo> getAppliedInvestments(String transNum);

    /***
     * 獲取選項
     * @return
     * @param policyNo
     */
    Map<String, List<Map<String, String>>> getCompanyAndCurrencyList(String policyNo);


    //查詢投資轉換詳情
    List<List<TransFundConversionVo>>  transInvestmentConversionDetail(String transNum)throws Exception;

    /**獲取保單投資比例大小限制數據
     *
     * @param policyNo  保單編號
     * @param policyType   保單類型
     * @return
     * @throws Exception
     */
    InvestmentPortfolioVo getInvestmentLimitSize(String policyNo,String  policyType)throws Exception;

    String checkHasApplying(String userId);

    //進行查詢當前保單是否已有投資標
    void handleNotExistingInvestmentLock(String userRocId, List<PolicyListVo> policyList)throws Exception;

    //查詢已有的投資的
    List<InvestmentPortfolioVo> getHeldInvestmentTarget(String policyNo)throws Exception;
    
    Map<String, Object> getSendMailInfo();
    //查詢臺幣與美元的單位大小
    Map<String, String> getNtdAndUsdUnitMin();
    //查詢臺幣與美元的比例大小
    Map<String, String> getNtdAndUsdProportionMin();

    BigDecimal getDistributeRationByInvtNo(String policyNo, String invtNo);
    //查詢當前的投資標商品是否為顯示賬戶
    List<String> getChckSwiftCode()throws Exception;
}
