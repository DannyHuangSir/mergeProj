package com.twfhclife.eservice_batch.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransMergeVo {
	
	/**  */
	private String transNum;
	/**  */
	private String transNumMerge;
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public String getTransNumMerge() {
		return this.transNumMerge;
	}
	
	public void setTransNumMerge(String transNumMerge) {
		this.transNumMerge = transNumMerge;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}