package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.BankInfoVo;

/**
 * 銀行資訊 Dao.
 * 
 * @author all
 */
public interface BankInfoDao {
	
	/**
	 * 銀行資訊-查詢.
	 * 
	 * @param bankInfoVo BankInfoVo
	 * @return 回傳查詢結果
	 */
	List<BankInfoVo> getBankInfo(@Param("bankInfoVo") BankInfoVo bankInfoVo);
}