package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.FundTransactionVo;

/**
 * 交易歷史記錄 Dao.
 * 
 * @author all
 */
public interface FundTransactionDao {

	/**
	 * 交易歷史記錄-分頁查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @param trCode 交易類別
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保交易歷史記錄
	 */
	List<FundTransactionVo> getFundTransactionPageList(
			@Param("policyNo") String policyNo, 
			@Param("trCode") String trCode,
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate, 
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);
	
	/**
	 * 交易歷史記錄-查詢.
	 * 
	 * @param fundTransactionVo FundTransactionVo
	 * @return 回傳保交易歷史記錄
	 */
	List<FundTransactionVo> getFundTransactionList(@Param("fundTransactionVo") FundTransactionVo fundTransactionVo);
	
	/**
	 * 取得保單的投資標的清單.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資標的清單
	 */
	List<FundTransactionVo> getInvtListByPolicyNo(@Param("policyNo") String policyNo);
	
	/**
	 * 期初餘額.
	 * 
	 * @param startDate 開始日期
	 * @param policyNo 保單號碼
	 * @param invtNo 投資標的代碼
	 * @return
	 */
	FundTransactionVo getOpeningBalance(
			@Param("startDate") String startDate, 
			@Param("policyNo") String policyNo,
			@Param("invtNo") String invtNo);
	
	/**
	 * 期初~期末餘額.
	 * 
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param policyNo 保單號碼
	 * @param invtNo 投資標的代碼
	 * @return
	 */
	List<FundTransactionVo> getInterimBalance(
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate,
			@Param("policyNo") String policyNo, 
			@Param("invtNo") String invtNo);
	
	/**
	 * 期末餘額.
	 * 
	 * @param endDate 結束日期
	 * @param policyNo 保單號碼
	 * @param invtNo 投資標的代碼
	 * @return
	 */
	FundTransactionVo getEndingBalance(
			@Param("endDate") String endDate, 
			@Param("policyNo") String policyNo,
			@Param("invtNo") String invtNo);
}