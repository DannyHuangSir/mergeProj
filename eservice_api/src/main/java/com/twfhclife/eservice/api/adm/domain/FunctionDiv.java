package com.twfhclife.eservice.api.adm.domain;

/**
 * FunctionDiv.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class FunctionDiv {

	/**
	 * DIV代碼.
	 */
	private Integer divId;

	/**
	 * DIV名稱.
	 */
	private String divName;

	/**
	 * 授權.
	 */
	private Boolean autho;

	public Integer getDivId() {
		return divId;
	}

	public void setDivId(Integer divId) {
		this.divId = divId;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public Boolean getAutho() {
		return autho;
	}

	public void setAutho(Boolean autho) {
		this.autho = autho;
	}

}
