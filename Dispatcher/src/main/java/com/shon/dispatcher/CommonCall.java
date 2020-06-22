package com.shon.dispatcher;

import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.imp.OnCallback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 15:50
 * Package name : com.shon.dispatcher
 * Des :
 */
final class CommonCall<T> implements TransCall<T> {

    private Transmitter transmitter;
    private OnCallback<T> onCallback;
    private ServiceMethod<Object, Object> serviceMethod;

    CommonCall(ServiceMethod<Object, Object> serviceMethod, Object[] args) {
        transmitter = serviceMethod.getTransmitter();
        this.serviceMethod = serviceMethod;
    }

    Transmitter getTransmitter() {
        return transmitter;
    }

    public void sendData() {
        Sender sender = serviceMethod.getCommand();
        if (sender == null) {
            return;
        }
        transmitter.sendData(sender.getSendCmd());
    }

    @Override
    public void execute(OnCallback<T> onCallback) {

        this.onCallback = onCallback;

//        TransRunnable transRunnable = new TransRunnable(this);
//        new Thread(transRunnable).start();
    }

    @Override
    public void cancel() {

    }

    void onDataCallback(Object object, Message message) {
        if (onCallback != null) {
            onCallback.onDataReceived((T) object, message);
        }
    }
}
