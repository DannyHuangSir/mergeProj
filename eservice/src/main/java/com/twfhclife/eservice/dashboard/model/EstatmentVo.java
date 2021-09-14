package com.twfhclife.eservice.dashboard.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EstatmentVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private BigDecimal id;
	/**  */
	private String estatmentType;
	/**  */
	private String estatmentTitle;
	/**  */
	private String estatmentSubtitle;
	/** 通知狀態 */
	private BigDecimal status;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date createDate;
	/**  */
	private String createUser;
	/**  */
	private String rocId;
	/** 通知月份  */
	private String transMonth;
	
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getEstatmentType() {
		return this.estatmentType;
	}
	
	public void setEstatmentType(String estatmentType) {
		this.estatmentType = estatmentType;
	}
	
	public String getEstatmentTitle() {
		return this.estatmentTitle;
	}
	
	public void setEstatmentTitle(String estatmentTitle) {
		this.estatmentTitle = estatmentTitle;
	}
	
	public String getEstatmentSubtitle() {
		return this.estatmentSubtitle;
	}
	
	public void setEstatmentSubtitle(String estatmentSubtitle) {
		this.estatmentSubtitle = estatmentSubtitle;
	}
	
	public BigDecimal getStatus() {
		return this.status;
	}
	
	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public String getRocId() {
		return this.rocId;
	}
	
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getTransMonth() {
		return transMonth;
	}

	public void setTransMonth(String transMonth) {
		this.transMonth = transMonth;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

