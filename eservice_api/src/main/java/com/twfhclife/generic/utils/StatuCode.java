package com.twfhclife.generic.utils;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
public enum StatuCode {
    AGREE_CODE("Y","可用"), DISAGREE_CODE("N","不可用(假刪除)");
    public   String  code;
    public   String  message;

    StatuCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
