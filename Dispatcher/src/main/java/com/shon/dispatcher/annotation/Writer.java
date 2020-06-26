package com.shon.dispatcher.annotation;

import com.shon.dispatcher.transer.Sender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:20
 * Package name : com.shon.dispatcher
 * Des : 发送 & 写入 数据
 */
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Writer {

    /**
     * 具体的写入实例
     *
     * @return Sender
     */
    Class<? extends Sender<?>> name();

    /**
     * 写入超时
     *
     * @return long time
     */
    long timeout() default 3 * 1000;
}
