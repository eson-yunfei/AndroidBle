package com.shon.dispatcher.transer;

import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:31
 * Package name : com.shon.dispatcher.bean
 * Des : 发送任务
 */
public abstract class Sender<T> implements ITrans<T> {


    /**
     * 获取需要发送的数据
     *
     * @return TMessage
     */
    public abstract TMessage getSendCmd(TMessage sendTMessage);

}
