package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransCustInfoVo;

/**
 * 變更基本資料 Dao.
 * 
 * @author all
 */
public interface TransCustInfoDao {
	
	/**
	 * 變更基本資料-查詢.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳查詢結果
	 */
	List<TransCustInfoVo> getTransCustInfo(@Param("transCustInfoVo") TransCustInfoVo transCustInfoVo);
	
	/**
	 * 變更基本資料-新增.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳影響筆數
	 */
	int insertTransCustInfo(@Param("transCustInfoVo") TransCustInfoVo transCustInfoVo);
}