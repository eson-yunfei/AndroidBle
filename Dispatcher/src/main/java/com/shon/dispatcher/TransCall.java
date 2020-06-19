package com.shon.dispatcher;

import com.shon.dispatcher.imp.OnCallback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:43
 * Package name : com.shon.dispatcher
 * Des :
 */
public interface TransCall<T> {
    void execute(OnCallback<T> onCallback);
    void cancel();
}
