package com.twfhclife.adm.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JdUserDetailReqVo implements Serializable {
    private String systemAccountOutput;
    private String userAccountStatusListOutput;
    private String userNameOutput;
    private String salesNumberOutput;
    private String deptSelectOutput;
    private String branchSelectOutput;
    private String roleNameSelectOutput;
    private List<String> column;
    private List<String>  columnName;

    private List<String> selectColumn = Lists.newArrayList();
    private List<String> unSelectColumn = Lists.newArrayList();
    private List<String> selectColumnName = Lists.newArrayList();
    private List<String> unSelectColumnName = Lists.newArrayList();


    public String getSystemAccountOutput() {
        return systemAccountOutput;
    }

    public void setSystemAccountOutput(String systemAccountOutput) {
        this.systemAccountOutput = systemAccountOutput;
    }

    public String getUserAccountStatusListOutput() {
        return userAccountStatusListOutput;
    }

    public void setUserAccountStatusListOutput(String userAccountStatusListOutput) {
        this.userAccountStatusListOutput = userAccountStatusListOutput;
    }

    public String getUserNameOutput() {
        return userNameOutput;
    }

    public void setUserNameOutput(String userNameOutput) {
        this.userNameOutput = userNameOutput;
    }

    public String getSalesNumberOutput() {
        return salesNumberOutput;
    }

    public void setSalesNumberOutput(String salesNumberOutput) {
        this.salesNumberOutput = salesNumberOutput;
    }

    public String getDeptSelectOutput() {
        return deptSelectOutput;
    }

    public void setDeptSelectOutput(String deptSelectOutput) {
        this.deptSelectOutput = deptSelectOutput;
    }

    public String getBranchSelectOutput() {
        return branchSelectOutput;
    }

    public void setBranchSelectOutput(String branchSelectOutput) {
        this.branchSelectOutput = branchSelectOutput;
    }

    public String getRoleNameSelectOutput() {
        return roleNameSelectOutput;
    }

    public void setRoleNameSelectOutput(String roleNameSelectOutput) {
        this.roleNameSelectOutput = roleNameSelectOutput;
    }

    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    public List<String> getSelectColumn() {
        return selectColumn;
    }

    public void setSelectColumn(List<String> selectColumn) {
        this.selectColumn = selectColumn;
    }

    public List<String> getUnSelectColumn() {
        return unSelectColumn;
    }

    public void setUnSelectColumn(List<String> unSelectColumn) {
        this.unSelectColumn = unSelectColumn;
    }

    public List<String> getSelectColumnName() {
        return selectColumnName;
    }

    public void setSelectColumnName(List<String> selectColumnName) {
        this.selectColumnName = selectColumnName;
    }

    public List<String> getUnSelectColumnName() {
        return unSelectColumnName;
    }

    public void setUnSelectColumnName(List<String> unSelectColumnName) {
        this.unSelectColumnName = unSelectColumnName;
    }
}
