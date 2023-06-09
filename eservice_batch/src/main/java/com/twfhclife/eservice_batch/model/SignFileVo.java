package com.twfhclife.eservice_batch.model;

public class SignFileVo {

   private String signFileId;
   private String ezAcquireTaskId;
   private String fileBase64;

   private String transNum;

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getSignFileId() {
        return signFileId;
    }

    public void setSignFileId(String signFileId) {
        this.signFileId = signFileId;
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
}
