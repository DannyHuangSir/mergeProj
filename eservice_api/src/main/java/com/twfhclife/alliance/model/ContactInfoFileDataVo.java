package com.twfhclife.alliance.model;

import java.util.Date;

/**
 * 此案件的上傳檔案狀態清單
 * @author hpe
 *
 */
public class ContactInfoFileDataVo {
	
	/**
	 * 檔案類型-診斷證明書
	 */
	public static final String TYPE_CERTIFICATE_DIAGNOSIS = "A";
	
	/**
	 * 檔案類型-收據
	 */
	public static final String TYPE_RECEIPT = "B";
	
	/**
	 * 作為首家公司可看到<br/>
	 * 等待處理中
	 */
	public static final String FILE_STATUS_PROCESSING = "0";
	
	/**
	 * 作為首家公司可看到<br/>
	 * 已上傳
	 */
	public static final String FILE_STATUS_UPLOADED = "1";
	
	/**
	 * 作為轉收公司可看到<br/>
	 * 可下載
	 */
	public static final String FILE_STATUS_ALLOW_DOWNLOAD = "2";
	
	public static final String FILE_STATUS_DOWNLOADED = "DOWNLOADED";
	
	private Float fdId;

	private Float contactSeqId;
	
	/**
	 * 聯盟通知件的Db Table sequence id
	 */
	private Float notifySeqId;
	
	/**
	 * 檔案編號</br>
	 * 長度：50
	 */
	private String fileId;
	
	/**
	 * 檔案大小</br>
	 * 單位：KB
	 */
	private String size;
	
	/**
	 * 檔案類型</br>
	 * String(1)"A":診斷證明書，"B":收據。
	 */
	private String type;
	
	/**
	 * 檔案名稱</br>
	 * 使用 FTP下載時的檔案名稱
	 */
	private String fileName;
	
	/**
	 * 檔案狀態</br>
	 * 作為首家公司可看到"0":等待處理中，"1":已上傳</br>
	 * 作為轉收公司可看到"2":可下載
	 */
	private String fileStatus;
	
	/**
	 * 檔案下載目錄</br>
	 * 使用 FTP/SFTP下載時的檔案路徑，為FTP/SFTP帳號登入後檔案於 Home目錄下的路徑。</br>
	 * /${首家代號}/${西元年月}/${案件編號}/</br>
	 * 例如：/L01/202003/wKODkASHSAiMmM5P77JdYg/
	 */
	private String path;
	
	/**
	 * 檔案上傳/下載執行後回傳訊息
	 */
	private String msg;
	
	private String sftpPath;
	
	private String fileBase64;
	private Float rfeId;
	private Date updateMsgDate;
	private Date createDate;


	public Float getRfeId() {
		return rfeId;
	}

	public void setRfeId(Float rfeId) {
		this.rfeId = rfeId;
	}

	public Date getUpdateMsgDate() {
		return updateMsgDate;
	}

	public void setUpdateMsgDate(Date updateMsgDate) {
		this.updateMsgDate = updateMsgDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Float getFdId() {
		return fdId;
	}

	public void setFdId(Float fdId) {
		this.fdId = fdId;
	}
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Float getContactSeqId() {
		return contactSeqId;
	}

	public void setContactSeqId(Float contactSeqId) {
		this.contactSeqId = contactSeqId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Float getNotifySeqId() {
		return notifySeqId;
	}

	public void setNotifySeqId(Float notifySeqId) {
		this.notifySeqId = notifySeqId;
	}

	public String getSftpPath() {
		return sftpPath;
	}

	public void setSftpPath(String sftpPath) {
		this.sftpPath = sftpPath;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}
	
	
}
