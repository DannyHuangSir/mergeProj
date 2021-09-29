package com.twfhclife.eservice_batch.model;

public class TransMedicalTreatmentClaimFileDatasVo {
	
	private Float fdId;
	
	private String transNum;
	
	private Float claimsSeqId;
	
	private String type;
	
	private String fileName;
	
	private String path;
	
	private Float  rfeId;
	
	private String ezAcquireTaskId;
	
	private String policyNo;
	
	private String lipiId;
	
	private String lipmId;
	
	private String fileBase64;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getEzAcquireTaskId() {
		return ezAcquireTaskId;
	}

	public void setEzAcquireTaskId(String ezAcquireTaskId) {
		this.ezAcquireTaskId = ezAcquireTaskId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getLipiId() {
		return lipiId;
	}

	public void setLipiId(String lipiId) {
		this.lipiId = lipiId;
	}

	public String getLipmId() {
		return lipmId;
	}

	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}

	public Float getFdId() {
		return fdId;
	}

	public void setFdId(Float fdId) {
		this.fdId = fdId;
	}

	public Float getClaimsSeqId() {
		return claimsSeqId;
	}

	public void setClaimsSeqId(Float claimsSeqId) {
		this.claimsSeqId = claimsSeqId;
	}

	public Float getRfeId() {
		return rfeId;
	}

	public void setRfeId(Float rfeId) {
		this.rfeId = rfeId;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}
	
}
