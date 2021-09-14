package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransContactInfoDtlVo;

/**
 * 變更保單聯絡資料修改後 Dao.
 * 
 * @author all
 */
public interface TransContactInfoDtlDao {
	
	/**
	 * 變更保單聯絡資料修改後-查詢.
	 * 
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return 回傳查詢結果
	 */
	List<TransContactInfoDtlVo> getTransContactInfoDtl(@Param("transContactInfoDtlVo") TransContactInfoDtlVo transContactInfoDtlVo);
	
	/**
	 * 變更保單聯絡資料修改後-新增.
	 * 
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return 回傳影響筆數
	 */
	int insertTransContactInfoDtl(@Param("transContactInfoDtlVo") TransContactInfoDtlVo transContactInfoDtlVo);
}