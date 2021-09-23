package com.twfhclife.adm.model;

/**
 * @author hui.chen
 * @create 2021-09-07
 */
public class HospitalInsuranceCompany {

    //編號
    public Integer id;
    //保險公司代碼
    public String insuranceId;
    //保險公司名稱
    public String insuranceName;
    //功能標識
    public  String   functionName;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
