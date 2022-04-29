package com.twfhclife.alliance.model;

/**
 * @author hui.chen
 * @create 2021-09-21
 */
public class MedicalTreatmentClaimFileDataVo {
	
    //ID編號
    private float fdId;
    //保單編號
    private float claimsSeqId;
    //檔案類型
    private String type;
    //檔案名稱
    private String fileName;
    //檔案路經
    private float rfeId;
    private String path;
    private String ezAcquireTaskId;
    //BASE64文件數據
    private String fileBase64;
    //FILE_ID
    private String fileId;
    //"檔案狀態	"
    private String fileStatus;
    //獲取聯盟的文件類型
    private String dtype;
    private String transNum;
    private String caseId;
    
    /**
     * Spec v1.5.2
     * get/set from MedicalTreatmentClaimFileTypeEnum
     * 醫療資料檔案類型
     */
    private String dtypes;


    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getFdId() {
        return fdId;
    }

    public void setFdId(float fdId) {
        this.fdId = fdId;
    }

    public float getClaimsSeqId() {
        return claimsSeqId;
    }

    public void setClaimsSeqId(float claimsSeqId) {
        this.claimsSeqId = claimsSeqId;
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

    public float getRfeId() {
        return rfeId;
    }

    public void setRfeId(float rfeId) {
        this.rfeId = rfeId;
    }

    public String getEzAcquireTaskId() {
        return ezAcquireTaskId;
    }

    public void setEzAcquireTaskId(String ezAcquireTaskId) {
        this.ezAcquireTaskId = ezAcquireTaskId;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * get/set from MedicalTreatmentClaimFileTypeEnum
     * @return String
     */
	public String getDtypes() {
		return dtypes;
	}

	/**
	 * get/set from MedicalTreatmentClaimFileTypeEnum
	 * @param dtypes
	 */
	public void setDtypes(String dtypes) {
		this.dtypes = dtypes;
	}
}
