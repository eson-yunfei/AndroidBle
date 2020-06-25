package com.shon.dispatcher;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 12:39
 * Package name : com.shon.dispatcher
 * Des :
 */
public abstract class InvocationHandler implements java.lang.reflect.InvocationHandler {
    public abstract void addMessage(TMessage receivedData);

    public abstract void sendSuccess(TMessage TMessage);
}
