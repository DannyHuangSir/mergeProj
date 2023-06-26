package com.twfhclife.alliance.domain;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Spa402RequestVo implements Serializable {
    private String orgId;
    private List<Spa402DataVo> data = Lists.newArrayList();
    private Long id;
    private String status;
    private String msg;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<Spa402DataVo> getData() {
        return data;
    }

    public void setData(List<Spa402DataVo> data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Spa402DataVo implements Serializable {

        private Long id;
        private String status;
        private String msg;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
