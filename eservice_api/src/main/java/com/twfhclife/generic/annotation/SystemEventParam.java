package com.twfhclife.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SystemEventParam {

	/**
	 * Mybatis sql id.
	 * 
	 * @return
	 */
	String sqlId() default "";

	/**
	 * 執行方法.
	 * 
	 * @return
	 */
	String execMethod();

	/**
	 * sql參數為vo物件.
	 * 
	 * @return
	 */
	String sqlVoType() default "";

	/**
	 * sql參數名稱.
	 * 
	 * @return
	 */
	String sqlVoKey() default "";

	/**
	 * Sql參數設定.
	 * 
	 * @return
	 */
	SqlParam[] sqlParams() default {};
	
	/**
	 * 存取物件-資料名稱.
	 * 
	 * @return
	 */
	String execFile() default "";
}
