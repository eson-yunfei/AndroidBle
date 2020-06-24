package com.shon.dispatcher.imp;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 16:04
 * Package name : com.shon.dispatcher.imp
 * Des :
 */
public interface OnMsgSendListener<T> extends OnMsgListener<T> {
    void onTimeout();
}
