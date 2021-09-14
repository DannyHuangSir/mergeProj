package com.twfhclife.adm.domain;

import java.io.Serializable;

import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.SystemEventVo;

public class EventRecordRequestVo implements Serializable {

	private static final long serialVersionUID = -4074568594557658480L;
	
	public BusinessEventVo businessEvent;
	
	public SystemEventVo systemEvent;

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
}
