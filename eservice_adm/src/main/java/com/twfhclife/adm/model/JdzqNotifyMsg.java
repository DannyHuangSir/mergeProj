package com.twfhclife.adm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class JdzqNotifyMsg {

    private List<String> users = Lists.newArrayList();

    private List<String> deps = Lists.newArrayList();

    private List<String> roles = Lists.newArrayList();

    private String msg;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getDeps() {
        return deps;
    }

    public void setDeps(List<String> deps) {
        this.deps = deps;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }
}
