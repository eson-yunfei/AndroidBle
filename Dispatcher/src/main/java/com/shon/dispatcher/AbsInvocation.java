package com.shon.dispatcher;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 12:39
 * Package name : com.shon.dispatcher
 * Des :
 */
public abstract class AbsInvocation implements java.lang.reflect.InvocationHandler {

    /**
     * 接收到数据
     *
     * @param receivedData receivedData
     */
    public abstract void addMessage(TMessage receivedData);


    /**
     * 发送成功
     *
     * @param tMessage tMessage
     */
    public abstract void sendSuccess(TMessage tMessage);
}
