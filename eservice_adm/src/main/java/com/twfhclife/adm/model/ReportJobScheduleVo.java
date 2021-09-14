package com.twfhclife.adm.model;

import java.util.Date;
import java.util.List;

import com.twfhclife.generic.model.Pagination;

/**
 * 工作排程
 * @author Ken Wei
 *
 */
public class ReportJobScheduleVo extends Pagination{

	/** 報表排程ID */
	private Integer reportJobId;
	
	/** 報表ID */
	private String reportId;
	
	/** 報表名稱 */
	private String reportName;
	
	/** 排程時間表達式 */
	private String cronExp;
	
	/** 執行命令 */
	private String executeCmd;
	
	/** 資料區間(天) */
	private Integer dateRange;
	
	private Date createDate;
	
	private String createUser;
	
	private Date modifyDate;
	
	private String modifyUser;
	
	/** 畫面顯示-報表條件 */
	private String scheduleConditionCht;
	
	/** 畫面輸入-排程條件 */
	private List<ReportJobConditionVo> listCondition;
	
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

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public String getExecuteCmd() {
		return executeCmd;
	}

	public void setExecuteCmd(String executeCmd) {
		this.executeCmd = executeCmd;
	}

	public Integer getDateRange() {
		return dateRange;
	}

	public void setDateRange(Integer dateRange) {
		this.dateRange = dateRange;
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

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getScheduleConditionCht() {
		return scheduleConditionCht;
	}

	public void setScheduleConditionCht(String scheduleConditionCht) {
		this.scheduleConditionCht = scheduleConditionCht;
	}

	public List<ReportJobConditionVo> getListCondition() {
		return listCondition;
	}

	public void setListCondition(List<ReportJobConditionVo> listCondition) {
		this.listCondition = listCondition;
	}

}
