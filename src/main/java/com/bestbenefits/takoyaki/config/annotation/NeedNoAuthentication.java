package com.bestbenefits.takoyaki.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//로그인이 필요하지 않은 메서드에 붙이는 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface NeedNoAuthentication {
}
