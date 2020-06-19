package com.shon.dispatcher.annotation;

import com.shon.dispatcher.bean.BaseCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:20
 * Package name : com.shon.dispatcher
 * Des :
 */
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface API {
    Class<? extends BaseCommand> name();
}
