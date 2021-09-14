package com.twfhclife.alliance.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DnsRequestVo extends BaseRequestVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> jobIds;

	public List<String> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
