package com.twfhclife.eservice.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

public class AgentVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String agentCode;
	/**  */
	private String abbrName;
	/**  */
	private String recCode;
	/**  */
	private BigDecimal aginSeq;
	/**  */
	private String tel1;
	/**  */
	private String tel2;
	/**  */
	private String inveArea;
	
	public String getAgentCode() {
		return this.agentCode;
	}
	
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	
	public String getAbbrName() {
		return this.abbrName;
	}
	
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}
	
	public String getRecCode() {
		return this.recCode;
	}
	
	public void setRecCode(String recCode) {
		this.recCode = recCode;
	}
	
	public BigDecimal getAginSeq() {
		return this.aginSeq;
	}
	
	public void setAginSeq(BigDecimal aginSeq) {
		this.aginSeq = aginSeq;
	}
	
	public String getTel1() {
		return this.tel1;
	}
	
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	
	public String getTel2() {
		return this.tel2;
	}
	
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	
	public String getInveArea() {
		return this.inveArea;
	}
	
	public void setInveArea(String inveArea) {
		this.inveArea = inveArea;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

