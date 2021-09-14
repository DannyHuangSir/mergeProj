package com.twfhclife.adm.model;

import java.util.Date;

public class SystemEventVo {

	private Integer businessEventId;
	private Date execDate;
	private String execMethod;
	private String execUser;
	private String execFile;
	private String execSql;
	private String execStatus;
	private Integer execCount;
	private String execMsg;
	private Date createDate;
	private String createUser;

	public Integer getBusinessEventId() {
		return businessEventId;
	}

	public void setBusinessEventId(Integer businessEventId) {
		this.businessEventId = businessEventId;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public String getExecMethod() {
		return execMethod;
	}

	public void setExecMethod(String execMethod) {
		this.execMethod = execMethod;
	}

	public String getExecUser() {
		return execUser;
	}

	public void setExecUser(String execUser) {
		this.execUser = execUser;
	}

	public String getExecFile() {
		return execFile;
	}

	public void setExecFile(String execFile) {
		this.execFile = execFile;
	}

	public String getExecSql() {
		return execSql;
	}

	public void setExecSql(String execSql) {
		this.execSql = execSql;
	}

	public String getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public Integer getExecCount() {
		return execCount;
	}

	public void setExecCount(Integer execCount) {
		this.execCount = execCount;
	}

	public String getExecMsg() {
		return execMsg;
	}

	public void setExecMsg(String execMsg) {
		this.execMsg = execMsg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
