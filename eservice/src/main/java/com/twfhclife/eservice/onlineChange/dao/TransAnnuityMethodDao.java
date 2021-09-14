package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;

/**
 * 變更年金給付方式 Dao.
 * 
 * @author all
 */
public interface TransAnnuityMethodDao {
	
	/**
	 * 變更年金給付方式-查詢.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳查詢結果
	 */
	List<TransAnnuityMethodVo> getTransAnnuityMethod(@Param("transAnnuityMethodVo") TransAnnuityMethodVo transAnnuityMethodVo);
	
	/**
	 * 變更年金給付方式-新增.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳影響筆數
	 */
	int insertTransAnnuityMethod(@Param("transAnnuityMethodVo") TransAnnuityMethodVo transAnnuityMethodVo);
	
	/**
	 * 取得外部舊年金保證期間
	 * @param policyNo
	 * @return String
	 */
	String getOldGuarTerm(@Param("policyNo") String policyNo);
}