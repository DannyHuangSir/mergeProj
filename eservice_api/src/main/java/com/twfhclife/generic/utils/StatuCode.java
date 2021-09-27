package com.twfhclife.generic.utils;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
public enum StatuCode {
    AGREE_CODE("Y","可用"), DISAGREE_CODE("N","不可用(假刪除)"),
    ACTION_CODE_Y("Y","取資料"), ACTION_CODE_N("N","不取資料");
    public   String  code;
    public   String  message;

    StatuCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
