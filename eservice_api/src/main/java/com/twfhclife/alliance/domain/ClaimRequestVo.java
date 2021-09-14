package com.twfhclife.alliance.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class ClaimRequestVo extends BaseRequestVo{

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
