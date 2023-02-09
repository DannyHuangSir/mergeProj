package com.twfhclife.eservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class IndividualVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String individualId;
	/**  */
	private String name;
	/**  */
	private String rocId;
	/**  */
	private String sex;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date birthDate;
	/**  */
	private String riskAttr;
	
	@JsonIgnore
	private String noHiddenRocId;
	
	public String getIndividualId() {
		return this.individualId;
	}
	
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRocId() {
		return this.rocId;
	}
	
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Date getBirthDate() {
		return this.birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getRiskAttr() {
		return this.riskAttr;
	}
	
	public void setRiskAttr(String riskAttr) {
		this.riskAttr = riskAttr;
	}

	public String getNoHiddenRocId() {
		return noHiddenRocId;
	}

	public void setNoHiddenRocId(String noHiddenRocId) {
		this.noHiddenRocId = noHiddenRocId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

