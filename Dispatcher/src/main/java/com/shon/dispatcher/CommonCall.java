package com.shon.dispatcher;

import android.os.Handler;

import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.imp.OnMsgSendListener;

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
    private TransRunnable transRunnable;

    boolean isWaiting;

    CommonCall(ServiceMethod<Object, Object> serviceMethod, Object[] args, Handler handler, TransRunnable transRunnable) {
        transmitter = serviceMethod.getTransmitter();
        this.serviceMethod = serviceMethod;
        this.myHandler = handler;
        this.transRunnable = transRunnable;
    }


    Transmitter getTransmitter() {
        return transmitter;
    }

    public boolean sendData() {
        Sender sender = serviceMethod.getCommand();
        if (sender == null) {
            return false;
        }
        transmitter.sendData(sender.getSendCmd());
        return startTime(300);
    }

    @Override
    public void execute(OnMsgSendListener<T> onCallback) {

        this.onCallback = onCallback;
        if (transRunnable != null) {
            transRunnable.addCall(this);
        }
    }



    boolean startTime(long timeout) {
        isWaiting = true;
        myHandler.postDelayed(timeoutRunnable, timeout);
        return true;
    }

    void cancelTimeOut() {
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

    void onDataCallback(final Object object, final Message message) {
        isWaiting = false;
        post(new Runnable() {
            @Override
            public void run() {
                if (onCallback != null) {
                    onCallback.onDataReceived((T) object, message);
                }
            }
        });

    }

    void post(Runnable runnable) {
        myHandler.post(runnable);
    }
}
