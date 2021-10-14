package com.twfhclife.alliance.model;

import java.sql.Timestamp;

/**
 * @author hui.chen
 * @create 2021-09-20
 */
public class NotifyOfNewCaseMedicalVo {
    /**
     * status default value
     */
    public static final String STATUS_DEFAULT = "0";
    public static final String STATUS_MESS = "新案件通知,待處理中";
    public static final String PRICE_STATUS_MESS = "首家案件,便於更新狀態.執行跑403";

    /**
     * 已取得查詢理賠資料
     */
    public static final String NC_STATUS_ONE  = "1";//已取得查詢理賠資料
    public static final String NC_STATUS_Y  = "Y";

    /**
     * 非台銀保戶
     */

    public static final String STATUS_3 = "3";//非保护或處理失敗
    public static final String MSG  = "非本公司保戶";//非台銀保戶
    public static final String GO_WORK_AHEAD_MSG  = "已轉為人工處理";//已有案件進行中

	// 編號
	private Float seqId;
	// 聯盟回壓案件編號
	private String caseId;
	// 案件類型
	private String type;
	// 觸發狀態類型
	private String ncStatus;
	// 觸發描述信息
	private String msg;
	// 案件狀態
	private String caseStatus;
	// 案件狀態描述
	private String caseMessage;

    private Timestamp createDate;

    private Timestamp statusDate;

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Timestamp statusDate) {
        this.statusDate = statusDate;
    }

    public Float getSeqId() {
        return seqId;
    }

    public void setSeqId(Float seqId) {
        this.seqId = seqId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseMessage() {
        return caseMessage;
    }

    public void setCaseMessage(String caseMessage) {
        this.caseMessage = caseMessage;
    }
}
