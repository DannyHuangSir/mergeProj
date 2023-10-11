package com.twfhclife.eservice.policy.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.PortfolioVo;

public interface PortfolioDao {

	/**
	 * 取得投資損益及投報率.
	 * 
	 * @param policyNo 保單號碼
	 * @param invtNo 投資標的方式(從投資停損表反查用)
	 * @return 回傳該保單下的投資損益及投報率
	 */
	List<PortfolioVo> getPortfolioList(@Param("policyNo") String policyNo, @Param("invtNo") List<String> invtNo);
	
	/**
	 * 取得基金下拉選單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳基金下拉選單資料
	 */
	List<PortfolioVo> getInvtOptionList(@Param("policyNo") String policyNo);
	
	/**
	 * 取得投資風險屬性.
	 * 
	 * @param insuCurr 幣別
	 * @return 取得幣別對應NTD匯率
	 */
	BigDecimal getNtdExchRate(@Param("insuCurr") String insuCurr);
	
	/**
	 * 取得投資風險屬性.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資風險屬性
	 */
	String getRiskLevelName(@Param("policyNo") String policyNo);
}