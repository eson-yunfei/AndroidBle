package com.shon.dispatcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 08:39
 * Package name : com.shon.dispatcher.annotation
 * Des :
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Param {
    String key();
}
