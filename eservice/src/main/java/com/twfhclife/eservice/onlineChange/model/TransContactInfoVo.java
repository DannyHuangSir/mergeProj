package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TransContactInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private BigDecimal id;
	/**  */
	private String transNum;
	
	private Float batchId;
	private String caseId;

	//文件数据
	private List<ContactInfoFileDataVo> fileDatas;

	public List<ContactInfoFileDataVo> getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(List<ContactInfoFileDataVo> fileDatas) {
		this.fileDatas = fileDatas;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getTransNum() {
		return this.transNum;
	}
	
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	public Float getBatchId() {
		return batchId;
	}

	public void setBatchId(Float batchId) {
		this.batchId = batchId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}