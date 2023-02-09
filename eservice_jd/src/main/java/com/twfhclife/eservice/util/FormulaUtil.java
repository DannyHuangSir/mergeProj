package com.twfhclife.eservice.util;

import java.math.BigDecimal;

public class FormulaUtil {

    public static BigDecimal[] formula2(BigDecimal netAmt, BigDecimal exchRate, BigDecimal ntdVal) {
        BigDecimal[] values = new BigDecimal[3];
        if (netAmt != null && exchRate != null && ntdVal != null) {
            values = RoiRateUtil.formula2(netAmt, exchRate, ntdVal);
        } else {
            values = getZero();
        }
        return values;
    }

    /** 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}% */
    public static BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
        BigDecimal[] values = new BigDecimal[3];
        if (netUnits != null && netValue != null && exchRate != null && ntdVal != null && accumAmt != null) {
            values = RoiRateUtil.formula1(netUnits, netValue, exchRate, ntdVal, accumAmt);
        } else {
            values = getZero();
        }
        return values;
    }

    /** 取0 */
    private static BigDecimal[] getZero() {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal[] values = { zero, zero, zero };
        return values;
    }
}
