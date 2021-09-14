package com.twfhclife.generic.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class FunctionUsageAdd {

	@NotEmpty(message = " cannot empty!")
	private String functionId;
	@NotEmpty(message = " cannot empty!")
	private String systemId;

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
