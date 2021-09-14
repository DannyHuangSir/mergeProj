package com.twfhclife.adm.model;

import java.util.List;

public class EventRecordVo {

	private List<SystemsVo> systems;
	private List<BusinessEventVo> businessEvents;
	private List<SystemEventVo> systemEvents;
	private List<ParameterVo> eventStatus;
	private List<ParameterVo> eventTypes;

	public List<SystemsVo> getSystems() {
		return systems;
	}

	public void setSystems(List<SystemsVo> systems) {
		this.systems = systems;
	}

	public List<BusinessEventVo> getBusinessEvents() {
		return businessEvents;
	}

	public void setBusinessEvents(List<BusinessEventVo> businessEvents) {
		this.businessEvents = businessEvents;
	}

	public List<SystemEventVo> getSystemEvents() {
		return systemEvents;
	}

	public void setSystemEvents(List<SystemEventVo> systemEvents) {
		this.systemEvents = systemEvents;
	}

	public List<ParameterVo> getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(List<ParameterVo> eventStatus) {
		this.eventStatus = eventStatus;
	}

	public List<ParameterVo> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(List<ParameterVo> eventTypes) {
		this.eventTypes = eventTypes;
	}

}
