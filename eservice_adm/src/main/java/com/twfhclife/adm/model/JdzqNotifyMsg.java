package com.twfhclife.adm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class JdzqNotifyMsg {

    private Long id;

    private List<String> users = Lists.newArrayList();

    private List<String> deps = Lists.newArrayList();

    private List<String> roles = Lists.newArrayList();

    private String msg;

    private String title;

    private String passageWay;
    private String notifyTarget;

    public void setRoleStr(String role) {
        if (StringUtils.isNotBlank(role)) {
            roles.addAll(Splitter.on(",").splitToList(role));
        }
    }

    public void setDepStr(String depStr) {
        if (StringUtils.isNotBlank(depStr)) {
            deps.addAll(Splitter.on(",").splitToList(depStr));
        }
    }

    public void setUserStr(String userStr) {
        if (StringUtils.isNotBlank(userStr)) {
            users.addAll(Splitter.on(",").splitToList(userStr));
        }
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotifyTarget() {
        return notifyTarget;
    }

    public void setNotifyTarget(String notifyTarget) {
        this.notifyTarget = notifyTarget;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassageWay() {
        return passageWay;
    }

    public void setPassageWay(String passageWay) {
        this.passageWay = passageWay;
    }
}
