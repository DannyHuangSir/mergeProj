package com.twfhclife.alliance.model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Bxcz414ReturnVo {

    private String code;
    private String msg;
    private Map<String, Object> data = Maps.newHashMap();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
