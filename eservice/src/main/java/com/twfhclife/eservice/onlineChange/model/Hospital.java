package com.twfhclife.eservice.onlineChange.model;

/**
 * @author hui.chen
 * @create 2021-09-02
 */
public class Hospital {
    //編號
    public  Integer   id;
    //醫院之醫事機構代碼
    public  String   hpid;
    //醫院名稱
    public  String   hpName;
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

    public String getHpid() {
        return hpid;
    }

    public void setHpid(String hpid) {
        this.hpid = hpid;
    }

    public String getHpName() {
        return hpName;
    }

    public void setHpName(String hpName) {
        this.hpName = hpName;
    }
}
