package com.twfhclife.generic.util;

import java.math.BigDecimal;

/** 計算報酬率 */
public class RoiRateUtil {
	
	private static final int DEF_SCALE = 4; // 除法運算精度
	
	//=============================================================================================
	
	/** 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}% */
	public static BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
		BigDecimal[] values = new BigDecimal[3];
		
		BigDecimal roiRate = null;
		BigDecimal numerator = MathUtil.add(MathUtil.mul(netUnits, netValue, exchRate), accumAmt); // 分子
		BigDecimal denominator = MathUtil.mul(ntdVal, netUnits); // 分母
		
		roiRate = denominator == new BigDecimal(0) ? new BigDecimal(0) : MathUtil.div(numerator, denominator, DEF_SCALE); 
		roiRate = MathUtil.sub(roiRate, new BigDecimal(1));		
		/*
		System.out.println("公式1: {[((單位數*單位淨值*匯率)+ 累積投資收益)/(平均台幣買價*總單位數)] – 1}%");
		System.out.println(roiRate + " = {[(" + netUnits + "*" + netValue + "*" + exchRate + ") / (" + ntdVal + "*" + netUnits + ")] – 1}%");
		System.out.println("[分子]:" + numerator);
		System.out.println("[分母]:" + denominator);
		System.out.println("投資配置金額=平均成本=平均單位成本*單位數:" + ntdVal + "*" + netUnits + "=" + MathUtil.mul(ntdVal, netUnits));
		System.out.println("--------------------------------------------------------------------------");
		*/
		values[0] = roiRate;
//		values[1] = MathUtil.mul(netUnits, netValue, exchRate); // 2014.05.26 晏生提參考帳戶價值=單位數*淨值*匯率
		values[1] = MathUtil.mul(netUnits, netValue, new BigDecimal(1));
		values[2] = denominator;
		
		return values;
	}
	
	//=============================================================================================
	
	/** 公式2: RT 貨幣帳戶: {[(帳戶金額*匯率)/平均台幣買價]-1}% */
	public static BigDecimal[] formula2(BigDecimal netAmt, BigDecimal exchRate, BigDecimal ntdVal) {
		BigDecimal[] values = new BigDecimal[3];
		
		BigDecimal roiRate = null;
		BigDecimal numerator = MathUtil.mul(netAmt, ntdVal); // 分子
		BigDecimal denominator = MathUtil.mul(netAmt, ntdVal); // 分母
		
		roiRate = MathUtil.div(numerator, denominator, DEF_SCALE); 
		roiRate = MathUtil.sub(roiRate, new BigDecimal(1));
		/*
		System.out.println("公式2: {[(帳戶金額*匯率)/平均台幣買價]-1}%");
		System.out.println(roiRate + " = {[(" + netAmt + "*" + exchRate + ") /" + ntdVal + "] – 1}%");
		System.out.println("[分子]:" + numerator);
		System.out.println("[分母]:" + denominator);		
		*/
		values[0] = roiRate;
		values[1] = numerator; // MathUtil.mul(netAmt, ntdVal); // 平均成本

		//2023.8.25 tangx sonar bugfix values[2] allways 0
//		values[2] = denominator;

		values[2] = new BigDecimal(0);
		return values;
	}

}
