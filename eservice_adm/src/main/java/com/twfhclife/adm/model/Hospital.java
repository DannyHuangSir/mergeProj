package com.twfhclife.adm.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-07
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

    private List<SubHospital> hpBranch = Lists.newArrayList();

    public List<SubHospital> getHpBranch() {
        return hpBranch;
    }

    public void setHpBranch(List<SubHospital> hpBranch) {
        this.hpBranch = hpBranch;
    }

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
