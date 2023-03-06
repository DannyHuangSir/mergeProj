package com.twfhclife.eservice.web.model;

import com.google.gson.annotations.SerializedName;

public class CancellationMoneyVo {

    @SerializedName("FSZ2-SCN-NAME")
    private String FSZ2_SCN_NAME;
    @SerializedName("FSZ2-FUNC-CODE")
    private String FSZ2_FUNC_CODE;
    @SerializedName("FSZ2-INSU-NO")
    private String FSZ2_INSU_NO;
    @SerializedName("FSZ2-CALC-DATE")
    private String FSZ2_CALC_DATE;
    @SerializedName("FSZ2-CALC-TYPE")
    private String FSZ2_CALC_TYPE;
    @SerializedName("FSZ2-AMT")
    private String FSZ2_AMT;
    @SerializedName("FSZ2-EFFECTIVE-DATE")
    private String FSZ2_EFFECTIVE_DATE;

    public String getFSZ2_SCN_NAME() {
        return FSZ2_SCN_NAME;
    }

    public void setFSZ2_SCN_NAME(String FSZ2_SCN_NAME) {
        this.FSZ2_SCN_NAME = FSZ2_SCN_NAME;
    }

    public String getFSZ2_FUNC_CODE() {
        return FSZ2_FUNC_CODE;
    }

    public void setFSZ2_FUNC_CODE(String FSZ2_FUNC_CODE) {
        this.FSZ2_FUNC_CODE = FSZ2_FUNC_CODE;
    }

    public String getFSZ2_INSU_NO() {
        return FSZ2_INSU_NO;
    }

    public void setFSZ2_INSU_NO(String FSZ2_INSU_NO) {
        this.FSZ2_INSU_NO = FSZ2_INSU_NO;
    }

    public String getFSZ2_CALC_DATE() {
        return FSZ2_CALC_DATE;
    }

    public void setFSZ2_CALC_DATE(String FSZ2_CALC_DATE) {
        this.FSZ2_CALC_DATE = FSZ2_CALC_DATE;
    }

    public String getFSZ2_CALC_TYPE() {
        return FSZ2_CALC_TYPE;
    }

    public void setFSZ2_CALC_TYPE(String FSZ2_CALC_TYPE) {
        this.FSZ2_CALC_TYPE = FSZ2_CALC_TYPE;
    }

    public String getFSZ2_AMT() {
        return FSZ2_AMT;
    }

    public void setFSZ2_AMT(String FSZ2_AMT) {
        this.FSZ2_AMT = FSZ2_AMT;
    }

    public String getFSZ2_EFFECTIVE_DATE() {
        return FSZ2_EFFECTIVE_DATE;
    }

    public void setFSZ2_EFFECTIVE_DATE(String FSZ2_EFFECTIVE_DATE) {
        this.FSZ2_EFFECTIVE_DATE = FSZ2_EFFECTIVE_DATE;
    }
}
