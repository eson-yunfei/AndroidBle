package com.shon.dispatcher.call;

import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 18:02
 * Package name : com.shon.dispatcher.call
 * Des :
 */
public interface ICall<T> {

    void onDataCallback(T result, TMessage TMessage);

}
