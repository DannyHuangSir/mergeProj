package com.twfhclife.eservice_batch.model;

import java.util.List;
import java.util.Map;

public class ReportJobVo {

	/** 報表排程ID */
	private Integer reportJobId;
	
	/** 報表ID */
	private String reportId;
	
	/** 報表名稱 */
	private String reportName;
	
	/** 資料區間(天) */
	private Integer dateRange;
	
	/** 排程條件 */
	private List<ReportJobConditionVo> listCondition;
	
	private Map<String, String> mapCondition;

	public Integer getReportJobId() {
		return reportJobId;
	}

	public void setReportJobId(Integer reportJobId) {
		this.reportJobId = reportJobId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Integer getDateRange() {
		return dateRange;
	}

	public void setDateRange(Integer dateRange) {
		this.dateRange = dateRange;
	}

	public List<ReportJobConditionVo> getListCondition() {
		return listCondition;
	}

	public void setListCondition(List<ReportJobConditionVo> listCondition) {
		this.listCondition = listCondition;
	}

	public Map<String, String> getMapCondition() {
		return mapCondition;
	}

	public void setMapCondition(Map<String, String> mapCondition) {
		this.mapCondition = mapCondition;
	}

}
