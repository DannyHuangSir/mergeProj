package com.twfhclife.adm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class MedicalTreatmentReportVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**申請日期區間*/
	private String startDate;
	
	private String endDate;
	
	/**申請來源*/
	private String fromCompanyId;
	
	/**申請項目*/
	private String applyItem;
	
	/**申請狀態*/
	private List<String> status;
	
	/** 保單號碼 */
	private String policyNo;
	
	/**被保人身分證字號*/
	private String lilipiIdNo;
	
	/**要保人身分證字號*/
	private String lilipmIdNo;
	
	/**授權傳送公司*/
	private List<String> companyList;
	
	/**是否傳送其他保險公司*/
	private String sendAlliance;

	
	
	
	private List<String>  column;
	
	private List<String>  columnName;

	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getFromCompanyId() {
		return fromCompanyId;
	}


	public void setFromCompanyId(String fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}


	public List<String> getCompanyList() {
		return companyList;
	}


	public void setCompanyList(List<String> companyList) {
		this.companyList = companyList;
	}

	public List<String> getStatus() {
		return status;
	}


	public void setStatus(List<String> status) {
		this.status = status;
	}


	public String getPolicyNo() {
		return policyNo;
	}


	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}


	public String getLilipiIdNo() {
		return lilipiIdNo;
	}


	public void setLilipiIdNo(String lilipiIdNo) {
		this.lilipiIdNo = lilipiIdNo;
	}


	public String getLilipmIdNo() {
		return lilipmIdNo;
	}


	public void setLilipmIdNo(String lilipmIdNo) {
		this.lilipmIdNo = lilipmIdNo;
	}


	public String getSendAlliance() {
		return sendAlliance;
	}


	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}


	public String getApplyItem() {
		return applyItem;
	}


	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}



	public List<String> getColumn() {
		return column;
	}


	public void setColumn(List<String> column) {
		this.column = column;
	}


	public List<String> getColumnName() {
		return columnName;
	}


	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
