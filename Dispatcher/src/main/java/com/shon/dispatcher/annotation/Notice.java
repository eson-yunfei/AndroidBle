package com.shon.dispatcher.annotation;

import com.shon.dispatcher.bean.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/19 22:17
 * Package name : com.shon.dispatcher.annotation
 * Des :
 */
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Notice {

    Class<? extends Listener> name();
}
