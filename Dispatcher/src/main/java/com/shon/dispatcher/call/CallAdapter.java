package com.shon.dispatcher.call;

import java.lang.reflect.Type;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:43
 * Package name : com.shon.dispatcher.core
 * Des :
 */
 public interface CallAdapter<R, T> {

    Type responseType();


    T adapt(ICall<R> call);

    ICall<R> getCall();

}
