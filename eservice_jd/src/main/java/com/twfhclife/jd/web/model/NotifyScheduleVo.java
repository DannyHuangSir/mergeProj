package com.twfhclife.jd.web.model;

import com.google.common.collect.Lists;

import java.util.List;

public class NotifyScheduleVo {

    private String userId;
    private List<SubNotifyScheduleVo> invts = Lists.newArrayList();

    public List<SubNotifyScheduleVo> getInvts() {
        return invts;
    }

    public void setInvts(List<SubNotifyScheduleVo> invts) {
        this.invts = invts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
