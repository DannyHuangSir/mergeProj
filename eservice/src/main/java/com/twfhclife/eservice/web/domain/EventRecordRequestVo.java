package com.twfhclife.eservice.web.domain;

import java.io.Serializable;

import com.twfhclife.eservice.web.model.BusinessEventVo;

public class EventRecordRequestVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BusinessEventVo businessEvent;

	public BusinessEventVo getBusinessEvent() {
		return businessEvent;
	}

	public void setBusinessEvent(BusinessEventVo businessEvent) {
		this.businessEvent = businessEvent;
	}
}