package com.twfhclife.eservice_batch.model;

import com.google.gson.annotations.SerializedName;

public class EZAcquireVo extends TransEndorsementVo {

	@SerializedName("Id")
	private String id;
	@SerializedName("IssueDate")
	private String issueDate;
	@SerializedName("TaskId")
	private String taskId;
	@SerializedName("DocumentId")
	private String documentId;
	@SerializedName("Status")
	private String status;
	
	private String documentType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

}
