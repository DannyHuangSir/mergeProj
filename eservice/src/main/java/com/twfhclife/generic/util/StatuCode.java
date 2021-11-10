package com.twfhclife.generic.util;

/**
 * @author hui.chen
 * @create 2021-06-28
 */
public enum StatuCode {
    AGREE_CODE("Y","同意"), DISAGREE_CODE("N","不同意"),
   ALLIANCE_Y("Y","已推送至聯盟並覆核人員審核"), ALLIANCE_N("C","帶覆核人員審核");
    public   String  code;
    public   String  message;

    StatuCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
