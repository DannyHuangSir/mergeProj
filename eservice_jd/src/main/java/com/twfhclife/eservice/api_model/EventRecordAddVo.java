package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.BusinessEventVo;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class EventRecordAddVo implements Serializable {

	private static final long serialVersionUID = 1335771205071009057L;

	@NotEmpty(message = " cannot empty!")
	public String sysId;

	@NotEmpty(message = " cannot empty!")
	public String userId;

	@NotEmpty(message = " cannot empty!")
	public List<BusinessEventVo> businessEventList = Lists.newArrayList();

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