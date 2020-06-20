package com.shon.dispatcher;

import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.imp.OnCallback;
import com.shon.dispatcher.utils.TransLog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 15:50
 * Package name : com.shon.dispatcher
 * Des :
 */
final class CommonCall<T> implements TransCall<T> {

    private int i = 1;
    private Transmitter transmitter;
    private OnCallback<T> onCallback;
    private ServiceMethod<Object, Object> serviceMethod;

     CommonCall(ServiceMethod<Object, Object> serviceMethod, Object[] args) {
        transmitter = serviceMethod.getTransmitter();
        this.serviceMethod = serviceMethod;
         TransLog.e("current i : " + i);
    }

    @Override
    public void execute(OnCallback<T> onCallback) {

         i = i + 1;
        TransLog.e("current i : " + i);
         this.onCallback = onCallback;
         Sender sender = serviceMethod.getCommand();
         if (sender == null){
             return;
         }
        transmitter.sendData(sender.getSendCmd());
//        onCallback.onDataReceived((T) "已执行");
    }

    @Override
    public void cancel() {

    }

    void onDataCallback(Object object, Message message){
        i = i + 1;
        TransLog.e("current i : " + i);
         if (onCallback != null){
             onCallback.onDataReceived((T) object,message);
         }
    }
}
