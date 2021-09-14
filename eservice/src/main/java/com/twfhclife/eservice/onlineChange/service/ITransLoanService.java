package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.model.TransLoanVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 申請保單貸款服務.
 * 
 * @author all
 */
public interface ITransLoanService {

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 申請保單貸款-查詢.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳查詢結果
	 */
	public List<TransLoanVo> getTransLoan(TransLoanVo transLoanVo);
	
	/**
	 * 申請保單貸款-新增.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳影響筆數
	 */
	public int insertTransLoan(TransLoanVo transLoanVo);
	
	/**
	 * 申請保單貸款-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransLoanVo getTransLoanDetail(String transNum);
	
	/**
	 * 取得申請保單貸款 byte[].
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransLoanVo transLoanVo);

	/**
	 * 過濾有指定帳號的保單.
	 * 
	 * @param policyList 保單清單資料
	 */
	public List<PolicyListVo> filterPolicyLoanAcct(List<PolicyListVo> policyList);
	
	/**
	 * 申請保單貸款(已有指定帳號)-新增
	 * 
	 * @param transLoanVo
	 * @param transExtendAttrList
	 * @return 回傳影響筆數
	 */
	public int insertTransLoanNew(TransLoanVo transLoanVo, List<TransExtendAttrVo> transExtendAttrList);
}