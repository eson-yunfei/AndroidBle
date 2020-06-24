package com.shon.dispatcher.call;

import com.shon.dispatcher.imp.OnMsgSendListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:43
 * Package name : com.shon.dispatcher
 * Des :
 */
public interface SenderCall<T> extends ICall<T>{
    void execute(OnMsgSendListener<T> onCallback);
}
