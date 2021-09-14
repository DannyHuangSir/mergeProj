package com.twfhclife.adm.domain;

import java.util.Date;

/**
 * Response bean for downloading user-authorization VSB.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class DownloadUserAuthCSVResponse {

	private String userAcct;

	private String role;

	private String functionName;

	private String sysName;
	
	private String firstName;
	
	private Date createDate;

	public String getUserAcct() {
		return userAcct;
	}

	public void setUserAcct(String userAcct) {
		this.userAcct = userAcct;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
