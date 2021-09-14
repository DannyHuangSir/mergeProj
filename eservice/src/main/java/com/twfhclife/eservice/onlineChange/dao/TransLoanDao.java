package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransLoanVo;

/**
 * 申請保單貸款 Dao.
 * 
 * @author all
 */
public interface TransLoanDao {
	
	/**
	 * 申請保單貸款-查詢.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳查詢結果
	 */
	List<TransLoanVo> getTransLoan(@Param("transLoanVo") TransLoanVo transLoanVo);
	
	/**
	 * 申請保單貸款-新增.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳影響筆數
	 */
	int insertTransLoan(@Param("transLoanVo") TransLoanVo transLoanVo);
}