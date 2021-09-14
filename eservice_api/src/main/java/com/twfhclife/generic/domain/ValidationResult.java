package com.twfhclife.generic.domain;

public class ValidationResult {

	private String field;
	private String rejectedResult;
	private String bindingFailure;
	private String objectName;
	private String codes;
	private String arguments;
	private String defaultMessage;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getRejectedResult() {
		return rejectedResult;
	}

	public void setRejectedResult(String rejectedResult) {
		this.rejectedResult = rejectedResult;
	}

	public String getBindingFailure() {
		return bindingFailure;
	}

	public void setBindingFailure(String bindingFailure) {
		this.bindingFailure = bindingFailure;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

}
