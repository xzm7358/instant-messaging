package com.sky.qq.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * xml文件注解
 * @author sky
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlName {
	String value();
}
