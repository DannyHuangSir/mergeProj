package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransBankInfoVo;

/**
 * 銀行帳戶資訊 Dao.
 * 
 * @author all
 */
public interface TransBankInfoDao {
	
	/**
	 * 銀行帳戶資訊-查詢.
	 * 
	 * @param transBankInfoVo TransBankInfoVo
	 * @return 回傳查詢結果
	 */
	List<TransBankInfoVo> getTransBankInfo(@Param("transBankInfoVo") TransBankInfoVo transBankInfoVo);
	
	/**
	 * 銀行帳戶資訊-新增.
	 * 
	 * @param transBankInfoVo TransBankInfoVo
	 * @return 回傳影響筆數
	 */
	int insertTransBankInfo(@Param("transBankInfoVo") TransBankInfoVo transBankInfoVo);
}