package com.twfhclife.eservice_batch.model;

public class ReportJobHistoryVo {
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";

	/** 報表排程ID */
	private Integer reportJobId;
	
	/** 執行時間 */
	private String executeTime;
	
	/** 執行狀態 */
	private String executeStatus;
	
	/** 錯誤訊息 */
	private String errorMsg;
	
	/** 檔案名稱 */
	private String fileName;
	
	/** 下載路徑 */
	private String downloadUrl;
	
	/** 紀錄的唯一值 */
	private Integer id;

	public Integer getReportJobId() {
		return reportJobId;
	}

	public void setReportJobId(Integer reportJobId) {
		this.reportJobId = reportJobId;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
