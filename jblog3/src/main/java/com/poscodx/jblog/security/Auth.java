package com.poscodx.jblog.security;

import static java.lang.annotation.ElementType.METHOD;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Auth {
	String value() default ""; // 기본값 설정
	public String id();
}