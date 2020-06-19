package com.shon.dispatcher.imp;

import com.shon.dispatcher.bean.Message;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 16:04
 * Package name : com.shon.dispatcher.imp
 * Des :
 */
public interface OnCallback<T> {
    void onDataReceived(T t, Message message);
}
