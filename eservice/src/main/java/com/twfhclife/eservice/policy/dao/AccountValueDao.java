package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.AcctValueVo;

/**
 * 帳戶價值 Dao.
 * 
 * @author alan
 */
public interface AccountValueDao {

	/**
	 * 取得帳戶價值通知書清單.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳該保單號碼下的所有帳戶價值通知書清單.
	 */
	List<AcctValueVo> getAccountValueList(@Param("policyNo") String policyNo);

	/**
	 * 取得帳戶價值通知書PDF資料.
	 * 
	 * @param policyNo 保單號碼
	 * @param quaterCode 年份季別
	 * @return 回傳帳戶價值通知書PDF資料.
	 */
	AcctValueVo getAccountValueData(@Param("policyNo") String policyNo, @Param("quaterCode") String quaterCode);

}
