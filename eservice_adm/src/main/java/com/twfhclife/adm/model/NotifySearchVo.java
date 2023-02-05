package com.twfhclife.adm.model;

import com.twfhclife.generic.model.Pagination;

public class NotifySearchVo extends Pagination {

    private String passageWay;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassageWay() {
        return passageWay;
    }

    public void setPassageWay(String passageWay) {
        this.passageWay = passageWay;
    }
}
