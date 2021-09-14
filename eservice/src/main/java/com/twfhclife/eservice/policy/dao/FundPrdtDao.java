package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.FundPrdtVo;

/**
 * 帳戶價值交易明細 Dao.
 * 
 * @author all
 */
public interface FundPrdtDao {
	
	/**
	 * 帳戶價值交易明細-查詢.
	 * 
	 * @param fundPrdtVo FundPrdtVo
	 * @return 回傳查詢結果
	 */
	List<FundPrdtVo> getFundPrdt(@Param("fundPrdtVo") FundPrdtVo fundPrdtVo);

	/**
	 * 取得保單的保費費用記錄(分頁).
	 * 
	 * @param policyNo 保單號碼
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保單的保費費用記錄
	 */
	List<FundPrdtVo> getFundPrdtPageList(
			@Param("policyNo") String policyNo,
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate, 
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);
}