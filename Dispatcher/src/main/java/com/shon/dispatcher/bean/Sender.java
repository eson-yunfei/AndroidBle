package com.shon.dispatcher.bean;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:31
 * Package name : com.shon.dispatcher.bean
 * Des :
 */
public abstract class Sender<T> implements ICommand<T> {
    /**
     * 获取需要发送的数据
     *
     * @return
     */
    public abstract Message getSendCmd();

}
