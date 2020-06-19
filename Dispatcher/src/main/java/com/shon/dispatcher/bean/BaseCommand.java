package com.shon.dispatcher.bean;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:31
 * Package name : com.shon.dispatcher.bean
 * Des :
 */
public abstract class BaseCommand<T> implements IMessage<T>{
    /**
     * 获取需要发送的数据
     *
     * @return
     */
    public abstract Message getSendCmd();

    /**
     * 处理消息，如果处理
     * @param message
     * @return
     */
    public abstract T handlerMessage(Message message);
}
