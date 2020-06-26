package com.shon.dispatcher.call;

import com.shon.dispatcher.call.ICall;
import com.shon.dispatcher.imp.OnMsgListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 17:52
 * Package name : com.shon.dispatcher.call
 * Des :
 */
public interface ListenerCall<T> extends ICall<T> {

    void onListener(OnMsgListener<T> msgListener);
}
