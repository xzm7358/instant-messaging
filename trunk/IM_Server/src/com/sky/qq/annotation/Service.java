package com.sky.qq.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 依赖注入注解类
 * @author sky
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
	String value() default "";
}
