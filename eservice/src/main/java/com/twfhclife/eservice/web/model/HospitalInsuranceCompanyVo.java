package com.twfhclife.eservice.web.model;

/**
 * @author hui.chen
 * @create 2021-09-15
 * 醫起通-醫院保險公司
 */
public class HospitalInsuranceCompanyVo {
    private Float id;
    //保險公司代碼
    public String insuranceId ;
    //保險公司名稱
    public String insuranceName ;
    //標識,那個功能上的
    public String functionName ;
    //時間標識
    public String createDate ;
    //當前狀態  Y  可用 N不可用(假刪除)
    public String status;

    public Float getId() {
        return id;
    }

    public void setId(Float id) {
        this.id = id;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
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
