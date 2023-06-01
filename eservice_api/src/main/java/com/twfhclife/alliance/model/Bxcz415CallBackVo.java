package com.twfhclife.alliance.model;

import com.twfhclife.eservice.onlineChange.model.Bxcz415CallBackDataVo;

public class Bxcz415CallBackVo {

   private String code;
   private String msg;
   private Bxcz415CallBackDataVo data;

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

    public Bxcz415CallBackDataVo getData() {
        return data;
    }

    public void setData(Bxcz415CallBackDataVo data) {
        this.data = data;
    }
}
