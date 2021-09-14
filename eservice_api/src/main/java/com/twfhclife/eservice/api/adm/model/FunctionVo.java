package com.twfhclife.eservice.api.adm.model;

import java.math.BigDecimal;
import java.util.List;

public class FunctionVo {
	
	private BigDecimal functionId;
	private String functionName;
	private String functionType;
	private String functionUrl;
	private String parentFuncId;
	private List<FunctionVo> subMenuList;
	private List<String> subFuntionUrlList;

	public BigDecimal getFunctionId() {
		return functionId;
	}

	public void setFunctionId(BigDecimal functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public String getParentFuncId() {
		return parentFuncId;
	}

	public void setParentFuncId(String parentFuncId) {
		this.parentFuncId = parentFuncId;
	}

	public List<FunctionVo> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<FunctionVo> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public List<String> getSubFuntionUrlList() {
		return subFuntionUrlList;
	}

	public void setSubFuntionUrlList(List<String> subFuntionUrlList) {
		this.subFuntionUrlList = subFuntionUrlList;
	}

	@Override
	public String toString() {
		return "[functionId=" + functionId + ", functionName=" + functionName + ", functionType="
				+ functionType + ", functionUrl=" + functionUrl + ", parentFuncId=" + parentFuncId + "]";
	}
}
