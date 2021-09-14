package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.InsuredVo;
import com.twfhclife.eservice.web.model.ProposerVo;

public class TravelPolicyVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/* 保單內容 Start */
	/** 旅遊國家類型 */
	private String travelDestType;

	/** 旅遊地點 */
	private String travelDest;

	/** 旅遊日期 */
	private String beginDate;

	/** 旅遊時間 */
	private String beginTime;

	/** 返抵日期 */
	private String endDate;

	/** 返抵時間 */
	private String endTime;

	/** 旅行天數 */
	private Integer travelDay;

	/* 不存DB值 */
	/** 當前要保人選擇的方案類型 */
	private String planType;

	/** 當前要保人選擇的旅平險保單方案內容 */
	private TravelPlanVo travelPlan;

	/* 保單內容 End */

	/* 其他資訊 Start */
	/** 是否有其他保險 */
	private String otherTravel;

	/** 保險公司名稱 */
	private String otherTravelCompany;

	/** 其他保險金額 */
	private BigDecimal otherAmtWrap;

	/** 預計領取保單地點 */
	private String wnpiDeptId;

	/** 預計領取保單地點-Parameter 名 */
	private String wnpiDeptStr;

	/* 其他資訊 End */

	/** 要保人 */
	private ProposerVo proposer;

	/** 被保人 */
	private InsuredVo insured;

	/** 受益人 */
	private BeneficiaryVo beneficiary;

	public String getTravelDestType() {
		return travelDestType;
	}

	public void setTravelDestType(String travelDestType) {
		this.travelDestType = travelDestType;
	}

	public String getTravelDest() {
		return travelDest;
	}

	public void setTravelDest(String travelDest) {
		this.travelDest = travelDest;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getTravelDay() {
		return travelDay;
	}

	public void setTravelDay(Integer travelDay) {
		this.travelDay = travelDay;
	}

	public TravelPlanVo getTravelPlan() {
		return travelPlan;
	}

	public void setTravelPlan(TravelPlanVo travelPlan) {
		this.travelPlan = travelPlan;
	}

	public String getOtherTravel() {
		return otherTravel;
	}

	public void setOtherTravel(String otherTravel) {
		this.otherTravel = otherTravel;
	}

	public String getOtherTravelCompany() {
		return otherTravelCompany;
	}

	public void setOtherTravelCompany(String otherTravelCompany) {
		this.otherTravelCompany = otherTravelCompany;
	}

	public BigDecimal getOtherAmtWrap() {
		return otherAmtWrap;
	}

	public void setOtherAmtWrap(BigDecimal otherAmtWrap) {
		this.otherAmtWrap = otherAmtWrap;
	}

	public String getWnpiDeptId() {
		return wnpiDeptId;
	}

	public void setWnpiDeptId(String wnpiDeptId) {
		this.wnpiDeptId = wnpiDeptId;
	}

	public String getWnpiDeptStr() {
		return wnpiDeptStr;
	}

	public void setWnpiDeptStr(String wnpiDeptStr) {
		this.wnpiDeptStr = wnpiDeptStr;
	}

	public ProposerVo getProposer() {
		return proposer;
	}

	public void setProposer(ProposerVo proposer) {
		this.proposer = proposer;
	}

	public InsuredVo getInsured() {
		return insured;
	}

	public void setInsured(InsuredVo insured) {
		this.insured = insured;
	}

	public BeneficiaryVo getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(BeneficiaryVo beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

}
