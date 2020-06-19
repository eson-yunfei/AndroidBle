package com.shon.dispatcher;

import java.lang.reflect.Type;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:43
 * Package name : com.shon.dispatcher.core
 * Des :
 */
 interface CallAdapter<R, T> {

    Type responseType();


    T adapt(TransCall<R> call);

    TransCall<R> getCall();

}
