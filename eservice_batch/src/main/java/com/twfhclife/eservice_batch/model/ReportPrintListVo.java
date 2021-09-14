package com.twfhclife.eservice_batch.model;

public class ReportPrintListVo {

	private String textContent;
	private Integer textOrder;
	
	public ReportPrintListVo(String textContent, Integer textOrder) {
		super();
		this.textContent = textContent;
		this.textOrder = textOrder;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public Integer getTextOrder() {
		return textOrder;
	}
	public void setTextOrder(Integer textOrder) {
		this.textOrder = textOrder;
	}
}
