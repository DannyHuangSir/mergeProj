package com.twfhclife.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventRecordParam {
	
	/**
	 * 業務事件.
	 * 
	 * @return
	 */
	String eventCode();
	
	/**
	 * 業務事件訊息.
	 * 
	 * @return
	 */
	String eventMsg() default "";
	
	/**
	 * Mybatis sql id.
	 * 
	 * @return
	 */
	String sqlId() default "";
	
	/**
	 * vo傳入sql中所使用的key
	 * 
	 * @return
	 */
	String daoVoParamKey() default "";

	/**
	 * Sql參數設定.
	 * 
	 * @return
	 */
	SqlParam[] sqlParams() default {};
}
