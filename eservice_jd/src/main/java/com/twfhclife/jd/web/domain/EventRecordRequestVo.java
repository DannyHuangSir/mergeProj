package com.twfhclife.jd.web.domain;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.jd.web.model.BusinessEventVo;
import com.twfhclife.jd.web.model.SystemEventVo;

public class EventRecordRequestVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BusinessEventVo businessEvent;

	public BusinessEventVo getBusinessEvent() {
		return businessEvent;
	}

	public void setBusinessEvent(BusinessEventVo businessEvent) {
		this.businessEvent = businessEvent;
	}

	public List<SystemEventVo> systemEventList;

	public List<SystemEventVo> getSystemEventList() {
		return systemEventList;
	}

	public void setSystemEventList(List<SystemEventVo> systemEventList) {
		this.systemEventList = systemEventList;
	}
}