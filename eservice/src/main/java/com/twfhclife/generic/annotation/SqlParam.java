package com.twfhclife.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlParam {

	/**
	 * 客戶端請求參數時所用的key值，預設用 requestParamkey 當sql參數的key值
	 * @return
	 */
	String requestParamkey();
	
	/**
	 * Mybatis sql中使用的參數key值。若sql的參數不是跟controller 所設定的一致才需要設定
	 * @return
	 */
	String sqlParamkey() default "";
}
