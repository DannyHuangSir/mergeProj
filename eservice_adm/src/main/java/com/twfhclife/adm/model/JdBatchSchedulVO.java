package com.twfhclife.adm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.generic.model.Pagination;

/**
 * @auther lihao
 */
public class JdBatchSchedulVO extends Pagination {
    private String batchId;
    private String batchStatus;

    private String batchName;

    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private String batchStartTime;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private String batchEndTime;
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private String batchJoinTime;
    private Integer failNum;

    private byte[] failLink;

    private String link;
    private String type;


    private byte[] batchFile;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public void setBatchFile(byte[] batchFile) {
        this.batchFile = batchFile;
    }

    public byte[] getBatchFile() {
        return batchFile;
    }

    public void setBatchJoinTime(String batchJoinTime) {
        this.batchJoinTime = batchJoinTime;
    }

    public String getBatchJoinTime() {
        return batchJoinTime;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public String getBatchStartTime() {
        return batchStartTime;
    }

    public String getBatchEndTime() {
        return batchEndTime;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }


    public void setLink(String link) {
        this.link = link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailLink(byte[] failLink) {
        this.failLink = failLink;
    }

    public byte[] getFailLink() {
        return failLink;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public void setBatchId(String batchNum) {
        this.batchId = batchNum;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public void setBatchStartTime(String batchStartTime) {
        this.batchStartTime = batchStartTime;
    }

    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

}
