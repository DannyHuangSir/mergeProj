package com.twfhclife.eservice.onlineChange.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransFundNotificationVo extends AbstractOnlineChangeModelBean {
	
	private static final long serialVersionUID = 1L;
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;

	private String fundNotificationDtlJsonData;
	
	private List<TransFundNotificationDtlVo> transFundNotificationDtlList;

	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getFundNotificationDtlJsonData() {
		return fundNotificationDtlJsonData;
	}

	public void setFundNotificationDtlJsonData(String fundNotificationDtlJsonData) {
		this.fundNotificationDtlJsonData = fundNotificationDtlJsonData;
	}

	public List<TransFundNotificationDtlVo> getTransFundNotificationDtlList() {
		return transFundNotificationDtlList;
	}

	public void setTransFundNotificationDtlList(List<TransFundNotificationDtlVo> transFundNotificationDtlList) {
		this.transFundNotificationDtlList = transFundNotificationDtlList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}