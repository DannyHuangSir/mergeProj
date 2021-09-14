package com.twfhclife.eservice_batch.util;

import java.math.BigDecimal;

public class MathUtil {	
	private static final int DEF_SCALE = 10; // 除法運算精度
	
	/** 加法運算 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		return v1.add(v2);
	} 
	
	/** 減法運算 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2);
	}
	
	/** 乘法運算 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		return v1.multiply(v2);
	}
	
	/** 乘法運算 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2, BigDecimal v3) {
		return v1.multiply(v2).multiply(v3);
	}
	
	/** 除法運算 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
		return MathUtil.div(v1, v2, DEF_SCALE);
	}
	
	/** 除法運算
	 * @param scale 例: scale=2, 表示取2位小數，多於的位數四捨五入 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
	}	
	
}
