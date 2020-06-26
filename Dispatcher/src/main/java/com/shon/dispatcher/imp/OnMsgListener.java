package com.shon.dispatcher.imp;

import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 17:48
 * Package name : com.shon.dispatcher.imp
 * Des :
 */
public interface OnMsgListener<T> {
    void onDataReceived(T t, TMessage tMessage);
}
