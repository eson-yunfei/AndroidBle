package com.shon.dispatcher.bean;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/19 22:19
 * Package name : com.shon.dispatcher.bean
 * Des :
 */
interface IMessage<T> {


    /**
     * 处理消息，如果处理,返回数据， 不处理 null
     * @param message
     * @param <T>
     * @return
     */
    <T> T handlerMessage(Message message);
}
