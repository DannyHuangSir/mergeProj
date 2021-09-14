package com.twfhclife.generic.api_model;

public class FunctionUsageAdd extends AbstractBaseDomain {

	private static final long serialVersionUID = 1L;
	
	private String functionId;
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
