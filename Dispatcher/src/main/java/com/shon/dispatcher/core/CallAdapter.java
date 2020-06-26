package com.shon.dispatcher.core;

import com.shon.dispatcher.call.ICall;

import java.lang.reflect.Type;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:43
 * Package name : com.shon.dispatcher.core
 * Des :
 */
interface CallAdapter<R, T> {

    Type responseType();


    T adapt(ICall<R> call);

    ICall<R> getCall();

}
