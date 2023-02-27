package com.twfhclife.adm.model;

import com.twfhclife.generic.model.Pagination;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @auther lihao
 */
public class JdUserVo extends Pagination implements Serializable {
    private String userId;
    private String userType;
    /**
     * 身份證字號
     */
    private String rocId;
    private String mobile;
    private String email;
    private String loginFailCount;
    private Date lastChangPasswordDate;
    private String smsFlag;
    private String mailFlag;
    private String fbId;
    private String moicaId;

    private Date crateDate;
    private String createUser;
    private String OnlineFlag;
    /**
     * 帳號狀態
     */
    private String status;
    private String userName;
    private Date loginTime;
    private String clauseFlag;
    private String serialNum;
    /**
     * 業務員編號
     */
    private String icId;
    /**
     * 登陸字段
     */
    private String loginSize;
    /**
     * 帳號生效日
     */
    private String effectiveDate;
    /**
     * 賬號失效日
     */
    private String expirationDate;

    /**
     * 所屬通路ID
     */
    private String depId;

    /**
     * 分支機構代碼
     */
    private String branchId;
    /**
     * 通路名稱
     */
    private String depName;
    /**
     * 分支機構名稱
     */
    private String branchName;
    /**
     * 角色Id
     */
    private String roleId;

    /**
     * 角色名稱
     */
    private String roleName;
    /**
     * 動作別
     */
    private String actionType;

    /**
     * 初始密碼
     */
    private String initPassword;

    /**
     * 失敗原因
     */
    private String failResult;
    private String link;

    private List<String> depBranchList;

    public void setDepBranchList(List<String> depBranchList) {
        this.depBranchList = depBranchList;
    }

    public List<String> getDepBranchList() {
        return depBranchList;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setFailResult(String failResult) {
        this.failResult = failResult;
    }

    public String getFailResult() {
        return failResult;
    }

    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }

    public String getInitPassword() {
        return initPassword;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepName() {
        return depName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }
    public String getDepId() {
        return depId;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRocId(String rocId) {
        this.rocId = rocId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoginFailCount(String loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public void setLastChangPasswordDate(Date lastChangPasswordDate) {
        this.lastChangPasswordDate = lastChangPasswordDate;
    }

    public void setSmsFlag(String smsFlag) {
        this.smsFlag = smsFlag;
    }

    public void setMailFlag(String mailFlag) {
        this.mailFlag = mailFlag;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void setMoicaId(String moicaId) {
        this.moicaId = moicaId;
    }

    public void setCrateDate(Date crateDate) {
        this.crateDate = crateDate;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setOnlineFlag(String onlineFlag) {
        OnlineFlag = onlineFlag;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public void setClauseFlag(String clauseFlag) {
        this.clauseFlag = clauseFlag;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public void setIcId(String icId) {
        this.icId = icId;
    }

    public void setLoginSize(String loginSize) {
        this.loginSize = loginSize;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getRocId() {
        return rocId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getLoginFailCount() {
        return loginFailCount;
    }

    public Date getLastChangPasswordDate() {
        return lastChangPasswordDate;
    }

    public String getSmsFlag() {
        return smsFlag;
    }

    public String getMailFlag() {
        return mailFlag;
    }

    public String getFbId() {
        return fbId;
    }

    public String getMoicaId() {
        return moicaId;
    }

    public Date getCrateDate() {
        return crateDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getOnlineFlag() {
        return OnlineFlag;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getClauseFlag() {
        return clauseFlag;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public String getIcId() {
        return icId;
    }

    public String getLoginSize() {
        return loginSize;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(failResult)
                .append(",").append(actionType)
                .append(",").append(userId)
                .append(",").append(status)
                .append(",").append(initPassword)
                .append(",").append(roleId)
                .append(",").append(effectiveDate)
                .append(",").append(expirationDate)
                .append(",").append(depId)
                .append(",").append(branchId)
                .append(",").append(userName)
                .append(",").append(icId)
                .append(",").append(loginSize)
                .append(",").append(rocId)
                .append(",").append(email)
                .append(",").append(mobile);
        return sb.toString();
    }
}
