package com.twfhclife.adm.model;

import java.io.Serializable;

public class UserDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String status;
    private String roleName;
    private String effectiveDate;
    private String expirationDate;
    private String depName;
    private String branchName;
    private String userName;
    private String icID;
    private String loginSize;
    private String rocID;
    private String email;
    private String mobile;
    private String createDate;

    private String loginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIcID() {
        return icID;
    }

    public void setIcID(String icID) {
        this.icID = icID;
    }

    public String getLoginSize() {
        return loginSize;
    }

    public void setLoginSize(String loginSize) {
        this.loginSize = loginSize;
    }

    public String getRocID() {
        return rocID;
    }

    public void setRocID(String rocID) {
        this.rocID = rocID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
