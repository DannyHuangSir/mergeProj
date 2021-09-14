package com.twfhclife.eservice.api.adm.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;

public class EventRecordAddVo implements Serializable {

	private static final long serialVersionUID = 1335771205071009057L;

	@NotEmpty(message = " cannot empty!")
	public String sysId;
	
	@NotEmpty(message = " cannot empty!")
	public String userId;
	
	@NotEmpty(message = " cannot empty!")
	public List<BusinessEventVo> businessEventList;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<BusinessEventVo> getBusinessEventList() {
		return businessEventList;
	}

	public void setBusinessEventList(List<BusinessEventVo> businessEventList) {
		this.businessEventList = businessEventList;
	}

	
}