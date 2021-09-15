package com.twfhclife.eservice.web.model;

/**
 * @author hui.chen
 * @create 2021-09-14
 * 醫院名稱實體
 */
public class HospitalVo {
    private Float id;
    //醫院之醫事機構代碼
    public String hpId;
    //醫院名稱
    public String hpName;
    //標識,那個功能上的
    public String functionName;
    //時間標識
    public String createDate;
    //當前狀態  Y  可用 N不可用(假刪除)
    public String status;

    public Float getId() {
        return id;
    }

    public void setId(Float id) {
        this.id = id;
    }

    public String getHpId() {
        return hpId;
    }

    public void setHpId(String hpId) {
        this.hpId = hpId;
    }

    public String getHpName() {
        return hpName;
    }

    public void setHpName(String hpName) {
        this.hpName = hpName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}