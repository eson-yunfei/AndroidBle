package com.shon.dispatcher.core;

import com.shon.dispatcher.TransCall;

import java.lang.reflect.Type;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:43
 * Package name : com.shon.dispatcher.core
 * Des :
 */
public interface CallAdapter<R, T> {

    Type responseType();


    T adapt(TransCall<R> call);


}
