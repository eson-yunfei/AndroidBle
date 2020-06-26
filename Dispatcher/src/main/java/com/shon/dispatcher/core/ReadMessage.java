package com.shon.dispatcher.core;

import com.shon.dispatcher.TMessage;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 15:06
 * Package name : com.shon.dispatcher.core
 * Des :
 */
public class ReadMessage implements Runnable {
    private LinkedBlockingQueue<TMessage> tMessages;
    private Invocation invocation;

    public ReadMessage(Invocation invocation) {
        tMessages = new LinkedBlockingQueue<>();
        this.invocation = invocation;
    }

    public void addMessage(TMessage receivedData) {
        tMessages.offer(receivedData);
    }


    @Override
    public void run() {

        while (true) {
            try {
                TMessage tMessage = tMessages.take();
                if (invocation == null) {
                    break;
                }
                invocation.handlerMessage(tMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
