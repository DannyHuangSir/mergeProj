package com.twfhclife.generic.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

public class SignStatusUtil {

    public static String signStatusToStr(String idVerifyStatus, String signStatus) {
        if (StringUtils.isNotBlank(idVerifyStatus) && !Lists.newArrayList("MID_S", "PBS_S", "EZ_OTP_S", "IDS_S").contains(idVerifyStatus)) {
            switch (idVerifyStatus) {
                case "CASE_I" : return "尚未身分驗證";
                case "EZ_OTP_F" : return "便利入口驗證失敗";
                case "EZ_OTP_E" : return "便利入口有效期限逾期";
                case "IDS_F" : return "身分驗證中心驗證失敗";
                case "IDS_E" : return "身分驗證中心有效期限逾期";
                case "ACT_E" : return "活動編碼有效期限逾期";
                case "MID_F" : return "MID驗證失敗";
                case "PBS_F" : return "保險存摺登入/註冊失敗";
                case "PBS_N" : return "保險存摺分非白金會員";
                case "MID_S_ID_F" : return "MID身分驗證完成身分證字號不符";
                case "PBS_S_ID_F" : return "保險存摺身分驗證身分證字號不符";
                case "EZ_OTP_S_ID_F " : return "便利入口驗證完成身分證字號不符";
                case "IDS_S_ID_F" : return "身分驗證中心驗證完成身分證字號不符";
            }
        } else if (StringUtils.isNotBlank(signStatus)) {
            switch (signStatus) {
                case "SIGN_I" : return "尚未簽署";
                case "SIGN_U_S" : return "保戶簽署完成";
                case "SIGN_L_S" : return "公會簽署完成";
                case "SIGN_U_F" : return "保戶簽署失敗";
                case "SIGN_L_F" : return "公會簽署失敗";
                case "SIGN_E" : return "文件簽署逾期";
            }
        } else {
            switch (idVerifyStatus) {
                case "MID_S" : return "MID身分驗證完成";
                case "PBS_S" : return "保險存摺身分驗證完成";
                case "EZ_OTP_S" : return "便利入口驗證完成";
                case "IDS_S" : return "身分驗證中心驗證完成";
            }
        }
        return "";
    }

    public static String signStatusToStr(String signStatus) {
        switch (signStatus) {
            case "SIGN_I" : return "尚未簽署";
            case "SIGN_U_S" : return "保戶簽署完成";
            case "SIGN_L_S" : return "公會簽署完成";
            case "SIGN_U_F" : return "保戶簽署失敗";
            case "SIGN_L_F" : return "公會簽署失敗";
            case "SIGN_E" : return "文件簽署逾期";
        }
        return "";
    }

    public static String verifyStatusToStr(String verifyStatus) {
        switch (verifyStatus) {
            case "CASE_I" : return "尚未身分驗證";
            case "EZ_OTP_F" : return "便利入口驗證失敗";
            case "EZ_OTP_E" : return "便利入口有效期限逾期";
            case "IDS_F" : return "身分驗證中心驗證失敗";
            case "IDS_E" : return "身分驗證中心有效期限逾期";
            case "ACT_E" : return "活動編碼有效期限逾期";
            case "MID_F" : return "MID驗證失敗";
            case "PBS_F" : return "保險存摺登入/註冊失敗";
            case "PBS_N" : return "保險存摺分非白金會員";
            case "MID_S_ID_F" : return "MID身分驗證完成身分證字號不符";
            case "PBS_S_ID_F" : return "保險存摺身分驗證身分證字號不符";
            case "EZ_OTP_S_ID_F " : return "便利入口驗證完成身分證字號不符";
            case "IDS_S_ID_F" : return "身分驗證中心驗證完成身分證字號不符";
            case "MID_S" : return "MID身分驗證完成";
            case "PBS_S" : return "保險存摺身分驗證完成";
            case "EZ_OTP_S" : return "便利入口驗證完成";
            case "IDS_S" : return "身分驗證中心驗證完成";
        }
        return "";
    }
}
