package com.twfhclife.alliance.model;

import com.twfhclife.alliance.domain.BaseRequestVo;

/**
 * @author hui.chen
 * @create 2021-09-20
 */
public class MedicalRequestVo  extends BaseRequestVo {

    private   Float  seqId;
    private   String  code;
    private   String  msg;

    @Override
    public String toString() {
        return "MedicalRequestVo{" +
                "seqId=" + seqId +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Float getSeqId() {
        return seqId;
    }

    public void setSeqId(Float seqId) {
        this.seqId = seqId;
    }

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
}
