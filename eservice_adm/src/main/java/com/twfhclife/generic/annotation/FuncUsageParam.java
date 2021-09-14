package com.twfhclife.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FuncUsageParam {

	/**
	 * Function Id
	 * <p>參照 FUNCTION_ITEM 設定</p>
	 * @return
	 */
	String funcId() default "";
	
	/**
	 * System Id
	 * <p>請依照使用系統設置</p>
	 * @return
	 */
	String systemId();
}
