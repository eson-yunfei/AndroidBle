package com.shon.dispatcher.core;

import android.os.Handler;

import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.ListenerCall;
import com.shon.dispatcher.imp.OnMsgListener;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 11:27
 * Package name : com.shon.dispatcher
 * Des :
 */
final class CommListenerCall<T> implements ListenerCall<T> {

    private OnMsgListener<T> onMsgListener;
    private Handler handler;

    public CommListenerCall(Handler handler) {
        this.handler = handler;
    }

    @Override
    public  void onListener(OnMsgListener<T> msgListener) {

        this.onMsgListener = msgListener;
    }


    @Override
    public void onDataCallback(final Object result, TMessage TMessage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (onMsgListener != null){
                    onMsgListener.onDataReceived((T) result, TMessage);
                }
            }
        });
    }
}
