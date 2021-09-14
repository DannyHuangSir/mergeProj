package com.twfhclife.eservice.api.elife.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.dashboard.model.EstatmentVo;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransVo;

public class DashboardResponse {

	/**
	 * 保障型的被保人資料
	 */
	private Map<String, List<String>> insuredProductData;

	/**
	 * 保障型商品數量
	 */
	private int benefitPolicyListSize;
	
	/**
	 * 投資型保單
	 */
	private List<PolicyListVo> invtPolicyList;
	
	/**
	 * 參考合計帳戶價值合計
	 */
	private BigDecimal policyAcctValueTotal;
	
	/**
	 * 我的資產＆負債
	 */
	private PolicyExtraVo policyExtraVo;
	
	/**
	 * 一個月內申請進度清單-已完成
	 */
	private List<TransVo> applyCompleteList;
	
	/**
	 * 一個月內申請進度清單-申請中.
	 */
	private List<TransVo> applyProcessingList;
	
	/**
	 * 事件通知資料
	 */
	private List<EstatmentVo> noticeBoardList;
	
	/**
	 * 未讀數量
	 */
	private BigDecimal notificationNotRead;
	
	/**
	 * 通知月份
	 */
	private List<String> noticeMonthList;

	public Map<String, List<String>> getInsuredProductData() {
		return insuredProductData;
	}

	public void setInsuredProductData(Map<String, List<String>> insuredProductData) {
		this.insuredProductData = insuredProductData;
	}

	public int getBenefitPolicyListSize() {
		return benefitPolicyListSize;
	}

	public void setBenefitPolicyListSize(int benefitPolicyListSize) {
		this.benefitPolicyListSize = benefitPolicyListSize;
	}

	public List<PolicyListVo> getInvtPolicyList() {
		return invtPolicyList;
	}

	public void setInvtPolicyList(List<PolicyListVo> invtPolicyList) {
		this.invtPolicyList = invtPolicyList;
	}

	public BigDecimal getPolicyAcctValueTotal() {
		return policyAcctValueTotal;
	}

	public void setPolicyAcctValueTotal(BigDecimal policyAcctValueTotal) {
		this.policyAcctValueTotal = policyAcctValueTotal;
	}

	public PolicyExtraVo getPolicyExtraVo() {
		return policyExtraVo;
	}

	public void setPolicyExtraVo(PolicyExtraVo policyExtraVo) {
		this.policyExtraVo = policyExtraVo;
	}

	public List<TransVo> getApplyCompleteList() {
		return applyCompleteList;
	}

	public void setApplyCompleteList(List<TransVo> applyCompleteList) {
		this.applyCompleteList = applyCompleteList;
	}

	public List<TransVo> getApplyProcessingList() {
		return applyProcessingList;
	}

	public void setApplyProcessingList(List<TransVo> applyProcessingList) {
		this.applyProcessingList = applyProcessingList;
	}

	public List<EstatmentVo> getNoticeBoardList() {
		return noticeBoardList;
	}

	public void setNoticeBoardList(List<EstatmentVo> noticeBoardList) {
		this.noticeBoardList = noticeBoardList;
	}

	public BigDecimal getNotificationNotRead() {
		return notificationNotRead;
	}

	public void setNotificationNotRead(BigDecimal notificationNotRead) {
		this.notificationNotRead = notificationNotRead;
	}

	public List<String> getNoticeMonthList() {
		return noticeMonthList;
	}

	public void setNoticeMonthList(List<String> noticeMonthList) {
		this.noticeMonthList = noticeMonthList;
	}
}