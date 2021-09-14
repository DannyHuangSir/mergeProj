package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransFundSwitchVo;
import com.twfhclife.eservice.policy.model.FundSwitchVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更投資標的及配置比例服務.
 * 
 * @author all
 */
public interface ITransFundSwitchService {
	
	/**
	 * 變更投資標的及配置比例-新增.
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return 回傳影響筆數
	 */
	public int insertTransFundSwitch(TransFundSwitchVo transFundSwitchVo);
	
	/**
	 * 變更投資標的及配置比例-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransFundSwitchVo getTransFundSwitchDetail(String transNum);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	public List<FundSwitchVo> getFundValueOptionList(String policyType);
	
	public void convertFundSwitch(TransFundSwitchVo transFundSwitchVo) throws Exception;
}