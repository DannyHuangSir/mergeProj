package com.twfhclife.eservice_batch.model;

import com.google.gson.annotations.SerializedName;

public class TransEndorsementExtVo extends TransEndorsementVo {

	@SerializedName("TaskId")
	private String taskId;
	@SerializedName("DocumentId")
	private String documentId;
	@SerializedName("Status")
	private String status;

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

}
