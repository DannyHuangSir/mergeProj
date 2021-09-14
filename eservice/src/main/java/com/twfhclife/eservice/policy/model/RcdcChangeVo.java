package com.twfhclife.eservice.policy.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RcdcChangeVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 保單號碼 */
	private String policyNo;
	/** 申請方式 */
	private String deliveryAgenCode;
	/** 申請項目 */
	private String changeItem;
	/** 申請時間 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date accDate;
	/** 處理進度 */
	private String procMk;

	private Integer sortNo;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getDeliveryAgenCode() {
		return deliveryAgenCode;
	}

	public void setDeliveryAgenCode(String deliveryAgenCode) {
		this.deliveryAgenCode = deliveryAgenCode;
	}

	public String getChangeItem() {
		return changeItem;
	}

	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem;
	}

	public Date getAccDate() {
		return accDate;
	}

	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}

	public String getProcMk() {
		return procMk;
	}

	public void setProcMk(String procMk) {
		this.procMk = procMk;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}