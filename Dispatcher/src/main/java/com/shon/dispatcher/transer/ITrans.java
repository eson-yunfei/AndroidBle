package com.shon.dispatcher.transer;

import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/19 22:19
 * Package name : com.shon.dispatcher.bean
 * Des :
 */
interface ITrans<T> {


    /**
     * 处理消息，如果处理,返回数据， 不处理 null
     * @param tMessage tMessage
     * @return T
     */
     T handlerMessage(TMessage tMessage);
}
