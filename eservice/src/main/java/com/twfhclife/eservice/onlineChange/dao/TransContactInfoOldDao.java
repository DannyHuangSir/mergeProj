package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransContactInfoOldVo;

/**
 * 變更保單聯絡資料修改前 Dao.
 * 
 * @author all
 */
public interface TransContactInfoOldDao {
	
	/**
	 * 變更保單聯絡資料修改前-查詢.
	 * 
	 * @param transContactInfoOldVo TransContactInfoOldVo
	 * @return 回傳查詢結果
	 */
	List<TransContactInfoOldVo> getTransContactInfoOld(@Param("transContactInfoOldVo") TransContactInfoOldVo transContactInfoOldVo);
	
	/**
	 * 變更保單聯絡資料修改前-新增.
	 * 
	 * @param transContactInfoOldVo TransContactInfoOldVo
	 * @return 回傳影響筆數
	 */
	int insertTransContactInfoOld(@Param("transContactInfoOldVo") TransContactInfoOldVo transContactInfoOldVo);
	
	/**
	 * get mobile from lipmda_es@db51
	 * @param policyNo
	 * @return
	 */
	String getPmdaMobile(@Param("policyNo") String policyNo);
	
	String getPmdaEmail(@Param("policyNo") String policyNo);
}
