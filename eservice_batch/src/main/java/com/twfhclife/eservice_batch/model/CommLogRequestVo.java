package com.twfhclife.eservice_batch.model;

import java.io.Serializable;

public class CommLogRequestVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CommLogRequestVo(String systemId, String commType, String commTarget,
			String commCnt) {
		super();
		this.systemId = systemId;
		this.commType = commType;
		this.commTarget = commTarget;
		this.commCnt = commCnt;
	}
	
	public CommLogRequestVo() {};

	private String systemId;
	private String commType;// mail or sms
	private String commTarget;// mail address or mobile
	private String commCnt;
	private String commDate;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getCommType() {
		return commType;
	}

	public void setCommType(String commType) {
		this.commType = commType;
	}

	public String getCommTarget() {
		return commTarget;
	}

	public void setCommTarget(String commTarget) {
		this.commTarget = commTarget;
	}

	public String getCommCnt() {
		return commCnt;
	}

	public void setCommCnt(String commCnt) {
		this.commCnt = commCnt;
	}

	public String getCommDate() {
		return commDate;
	}

	public void setCommDate(String commDate) {
		this.commDate = commDate;
	}

}
