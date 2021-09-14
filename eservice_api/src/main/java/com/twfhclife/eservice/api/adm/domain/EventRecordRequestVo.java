package com.twfhclife.eservice.api.adm.domain;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;

public class EventRecordRequestVo implements Serializable {

	private static final long serialVersionUID = -8911686209755505080L;

	public BusinessEventVo businessEvent;
	
	public SystemEventVo systemEvent;
	
	public List<SystemEventVo> systemEventList;

	public BusinessEventVo getBusinessEvent() {
		return businessEvent;
	}

	public void setBusinessEvent(BusinessEventVo businessEvent) {
		this.businessEvent = businessEvent;
	}

	public SystemEventVo getSystemEvent() {
		return systemEvent;
	}

	public void setSystemEvent(SystemEventVo systemEvent) {
		this.systemEvent = systemEvent;
	}

	public List<SystemEventVo> getSystemEventList() {
		return systemEventList;
	}

	public void setSystemEventList(List<SystemEventVo> systemEventList) {
		this.systemEventList = systemEventList;
	}
}