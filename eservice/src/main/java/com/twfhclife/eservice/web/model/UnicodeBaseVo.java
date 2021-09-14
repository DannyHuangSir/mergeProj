package com.twfhclife.eservice.web.model;

import java.io.Serializable;

public class UnicodeBaseVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String internalCode;
	private String internalName;
	private String utf16Code;
	private String tencarry;

	public String getInternalCode() {
		return internalCode;
	}

	public void setInternalCode(String internalCode) {
		this.internalCode = internalCode;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getUtf16Code() {
		return utf16Code;
	}

	public void setUtf16Code(String utf16Code) {
		this.utf16Code = utf16Code;
	}

	public String getTencarry() {
		return tencarry;
	}

	public void setTencarry(String tencarry) {
		this.tencarry = tencarry;
	}

}
