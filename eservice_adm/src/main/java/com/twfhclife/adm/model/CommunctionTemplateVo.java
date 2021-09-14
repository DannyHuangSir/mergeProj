package com.twfhclife.adm.model;

import java.util.List;

public class CommunctionTemplateVo {

	private List<SystemsVo> systems;
	private List<ParameterVo> templateStatus;
	private List<MessagingTemplateVo> MessagingTemplates;
	private List<ParameterVo> eventTypes;
	private List<ParameterVo> parameters;

	public List<SystemsVo> getSystems() {
		return systems;
	}

	public void setSystems(List<SystemsVo> systems) {
		this.systems = systems;
	}

	public List<ParameterVo> getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(List<ParameterVo> templateStatus) {
		this.templateStatus = templateStatus;
	}

	public List<MessagingTemplateVo> getMessagingTemplates() {
		return MessagingTemplates;
	}

	public void setMessagingTemplates(List<MessagingTemplateVo> MessagingTemplates) {
		this.MessagingTemplates = MessagingTemplates;
	}

	public List<ParameterVo> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(List<ParameterVo> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public List<ParameterVo> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterVo> parameters) {
		this.parameters = parameters;
	}

}
