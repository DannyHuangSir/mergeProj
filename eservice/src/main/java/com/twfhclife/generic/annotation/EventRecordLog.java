package com.twfhclife.generic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventRecordLog {
	
	/**
	 * 業務事件設定參數.
	 * 
	 * @return
	 */
	EventRecordParam value();
}
