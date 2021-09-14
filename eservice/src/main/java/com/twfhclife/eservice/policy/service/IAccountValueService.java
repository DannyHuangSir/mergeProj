package com.twfhclife.eservice.policy.service;

import java.util.List;

import com.twfhclife.eservice.policy.model.AcctValueVo;

/**
 * 帳戶價值服務.
 * 
 * @author alan
 */
public interface IAccountValueService {

	/**
	 * 取得帳戶價值通知書清單.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳該保單號碼下的所有帳戶價值通知書清單.
	 */
	public List<AcctValueVo> getAccountValueList(String policyNo);

	/**
	 * 取得帳戶價值通知書PDF資料.
	 * 
	 * @param policyNo 保單號碼
	 * @param quaterCode 年份季別
	 * @return 回傳帳戶價值通知書PDF資料.
	 */
	public byte[] getAccountValueData(String policyNo, String quaterCode);

}
