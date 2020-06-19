package com.shon.dispatcher;

import com.shon.dispatcher.bean.BaseCommand;
import com.shon.dispatcher.core.ServiceMethod;
import com.shon.dispatcher.imp.OnCallback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 15:50
 * Package name : com.shon.dispatcher
 * Des :
 */
final class CommonCall<T> implements TransCall<T> {

    private Transmitter transmitter;
    private ServiceMethod<Object, TransCall<?>> serviceMethod;

    public CommonCall(ServiceMethod<Object, TransCall<?>> serviceMethod, T args) {
        transmitter = serviceMethod.getTransmitter();
        this.serviceMethod = serviceMethod;
    }

    @Override
    public void execute(OnCallback<T> onCallback) {

         BaseCommand baseCommand = serviceMethod.getCommand();
         if (baseCommand == null){
             return;
         }
        transmitter.sendData(baseCommand.getSendCmd());
        onCallback.onDataReceived((T) "已执行");
    }
}
