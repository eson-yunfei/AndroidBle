package com.shon.dispatcher.core;

import android.os.Handler;

import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.imp.OnMsgSendListener;
import com.shon.dispatcher.transer.Sender;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 15:50
 * Package name : com.shon.dispatcher
 * Des :
 */
final class CommonCall<T> implements SenderCall<T> {

    private Transmitter transmitter;
    private OnMsgSendListener<T> onCallback;
    private ServiceMethod<Object, Object> serviceMethod;
    private Handler myHandler;
    private SendRunnable sendRunnable;
    private Object[] args;
    private TMessage tMessage;
    boolean isWaiting;

    CommonCall(ServiceMethod<Object, Object> serviceMethod, Object[] args, Handler handler, SendRunnable sendRunnable) {
        transmitter = serviceMethod.getTransmitter();
        this.serviceMethod = serviceMethod;
        this.args = args;
        this.myHandler = handler;
        this.sendRunnable = sendRunnable;
    }


    Transmitter getTransmitter() {
        return transmitter;
    }

    public boolean sendData() {
        Sender<?> sender = serviceMethod.getCommand();
        if (sender == null) {
            return false;
        }
        if (args == null || args.length == 0) {
            return false;
        }

        tMessage = sender.getSendCmd((TMessage) args[0]);

        transmitter.sendData(tMessage);
        return startTime(500);
    }
    public String getSendMsg(){
        return tMessage.getBytes().toString();
    }

    @Override
    public void execute(OnMsgSendListener<T> onCallback) {

        this.onCallback = onCallback;
        if (sendRunnable != null) {
            sendRunnable.addCall(this);
        }
    }


    boolean startTime(long timeout) {
        isWaiting = true;
        myHandler.postDelayed(timeoutRunnable, timeout);
        return true;
    }

    @Override
    public void cancelTimeOut() {
        isWaiting = false;
        myHandler.removeCallbacks(timeoutRunnable);
    }


    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            isWaiting = false;
            if (onCallback != null) {
                onCallback.onTimeout();
            }
            myHandler.removeCallbacks(this);
        }
    };

    @Override
    public void onDataCallback(final Object object, final TMessage TMessage) {
        isWaiting = false;
        post(new Runnable() {
            @Override
            public void run() {
                if (onCallback != null) {
                    onCallback.onDataReceived((T) object, TMessage);
                }
            }
        });

    }

    void post(Runnable runnable) {
        myHandler.post(runnable);
    }
}
